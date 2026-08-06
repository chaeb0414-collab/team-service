package com.example.team_service.member;

import com.example.team_service.common.NotFoundException;
import com.example.team_service.member.dto.ProfileImageResponse;
import com.example.team_service.member.dto.ProfileImageUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * 팀원 프로필 이미지를 S3에 업로드하고, Presigned URL로 조회할 수 있게 한다.
 *
 * DB에는 이미지 원본 URL이 아니라 S3 객체 key만 저장하고, 실제 접근 가능한 URL은
 * 조회 시점에 유효기간 7일짜리 Presigned URL로 매번 새로 발급한다.
 */
@Service
@Transactional(readOnly = true)
public class ProfileImageService {

    private static final Duration PRESIGNED_URL_DURATION = Duration.ofDays(7);

    private final MemberRepository memberRepository;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket:}")
    private String bucket;

    public ProfileImageService(
            MemberRepository memberRepository,
            S3Client s3Client,
            S3Presigner s3Presigner
    ) {
        this.memberRepository = memberRepository;
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Transactional
    public ProfileImageResponse uploadProfileImage(Long memberId, MultipartFile file) {

        Member member = findMember(memberId);

        String key = buildKey(memberId, file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new IllegalStateException("프로필 이미지 업로드에 실패했습니다.", e);
        }

        member.updateProfileImageKey(key);

        return new ProfileImageResponse(member.getId(), member.getProfileImageKey());
    }

    public ProfileImageUrlResponse getProfileImageUrl(Long memberId) {

        Member member = findMember(memberId);

        String key = member.getProfileImageKey();

        if (key == null) {
            throw new NotFoundException("등록된 프로필 이미지가 없습니다. id=" + memberId);
        }

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(PRESIGNED_URL_DURATION)
                .getObjectRequest(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build())
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return new ProfileImageUrlResponse(
                presignedRequest.url().toString(),
                Instant.now().plus(PRESIGNED_URL_DURATION)
        );
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "팀원을 찾을 수 없습니다. id=" + memberId
                        )
                );
    }

    private String buildKey(Long memberId, String originalFilename) {
        String safeName = (originalFilename == null || originalFilename.isBlank())
                ? "image"
                : originalFilename;

        return "profile-images/%d/%s-%s".formatted(memberId, UUID.randomUUID(), safeName);
    }
}

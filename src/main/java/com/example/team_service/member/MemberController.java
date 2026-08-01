package com.example.team_service.member;

import com.example.team_service.member.dto.MemberCreateRequest;
import com.example.team_service.member.dto.MemberResponse;
import com.example.team_service.member.dto.ProfileImageResponse;
import com.example.team_service.member.dto.ProfileImageUrlResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final ProfileImageService profileImageService;

    public MemberController(
            MemberService memberService,
            ProfileImageService profileImageService
    ) {
        this.memberService = memberService;
        this.profileImageService = profileImageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(
            @RequestBody MemberCreateRequest request
    ) {
        return memberService.createMember(request);
    }

    @GetMapping("/{id}")
    public MemberResponse getMember(
            @PathVariable Long id
    ) {
        return memberService.getMember(id);
    }

    @PostMapping("/{id}/profile-image")
    public ProfileImageResponse uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return profileImageService.uploadProfileImage(id, file);
    }

    @GetMapping("/{id}/profile-image")
    public ProfileImageUrlResponse getProfileImage(
            @PathVariable Long id
    ) {
        return profileImageService.getProfileImageUrl(id);
    }
}
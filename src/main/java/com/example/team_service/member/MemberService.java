package com.example.team_service.member;

import com.example.team_service.common.NotFoundException;
import com.example.team_service.member.dto.MemberCreateRequest;
import com.example.team_service.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {

        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );

        Member savedMember = memberRepository.save(member);

        return new MemberResponse(savedMember);
    }

    public MemberResponse getMember(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "팀원을 찾을 수 없습니다. id=" + id
                        )
                );

        return new MemberResponse(member);
    }
}
package com.example.team_service.member;

import com.example.team_service.member.dto.MemberCreateRequest;
import com.example.team_service.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
}
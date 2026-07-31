package com.example.team_service.member.dto;

import com.example.team_service.member.Member;

public class MemberResponse {

    private final Long id;

    private final String name;

    private final Integer age;

    private final String mbti;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.age = member.getAge();
        this.mbti = member.getMbti();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getMbti() {
        return mbti;
    }
}
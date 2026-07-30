package com.example.team_service.member.dto;

public record MemberCreateRequest(
        String name,
        Integer age,
        String mbti
) {
}
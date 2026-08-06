package com.example.team_service.member.dto;

public class ProfileImageResponse {

    private final Long memberId;

    private final String profileImageKey;

    public ProfileImageResponse(Long memberId, String profileImageKey) {
        this.memberId = memberId;
        this.profileImageKey = profileImageKey;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getProfileImageKey() {
        return profileImageKey;
    }
}

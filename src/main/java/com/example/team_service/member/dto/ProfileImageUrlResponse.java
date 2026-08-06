package com.example.team_service.member.dto;

import java.time.Instant;

public class ProfileImageUrlResponse {

    private final String url;

    private final Instant expiresAt;

    public ProfileImageUrlResponse(String url, Instant expiresAt) {
        this.url = url;
        this.expiresAt = expiresAt;
    }

    public String getUrl() {
        return url;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}

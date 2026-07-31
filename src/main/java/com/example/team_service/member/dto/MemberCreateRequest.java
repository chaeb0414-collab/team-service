package com.example.team_service.member.dto;

public class MemberCreateRequest {

    private String name;
    private Integer age;
    private String mbti;

    public MemberCreateRequest() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setHobby(Integer age) {
        this.age = age;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }
}
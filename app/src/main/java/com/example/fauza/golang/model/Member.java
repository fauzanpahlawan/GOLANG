package com.example.fauza.golang.model;

public class Member {

    private String memberName;
    private String mobileNumher;
    private String email;
    private String type;
    private String photo;
    private float ratingPoin;
    private float ratingVoter;

    public Member() {
    }

    public Member(String memberName, String mobileNumher, String email, String type, String photo, float ratingPoin, float ratingVoter) {
        this.memberName = memberName;
        this.mobileNumher = mobileNumher;
        this.email = email;
        this.type = type;
        this.photo = photo;
        this.ratingPoin = ratingPoin;
        this.ratingVoter = ratingVoter;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMobileNumher() {
        return mobileNumher;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getPhoto() {
        return photo;
    }

    public float getRatingPoin() {
        return ratingPoin;
    }

    public float getRatingVoter() {
        return ratingVoter;
    }
}

package com.example.fauza.golang.model;

public class Member {

    public String memberName;
    public String mobileNumher;
    public String email;
    public String type;
    public String photo;
    public float ratingPoin;
    public float ratingVoter;

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

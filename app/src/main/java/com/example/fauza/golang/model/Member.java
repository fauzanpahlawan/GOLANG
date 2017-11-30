package com.example.fauza.golang.model;

public class Member {

    public String memberName;
    public String mobileNumher;
    public String email;
    public String type;
    public String tourId;

    public Member() {
    }

    public Member(String memberName, String mobileNumher, String email, String type, String tourId) {
        this.memberName = memberName;
        this.mobileNumher = mobileNumher;
        this.email = email;
        this.type = type;
        this.tourId = tourId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMobileNumher() {
        return mobileNumher;
    }

    public void setMobileNumher(String mobileNumher) {
        this.mobileNumher = mobileNumher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
}

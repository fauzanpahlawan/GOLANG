package com.example.fauza.golang;

public class Member {

    public String memberName;
    public String mobileNumher;
    public String email;
    public String type;

    public Member() {
    }

    public Member(String memberName, String mobileNumher, String email, String type) {
        this.memberName = memberName;
        this.mobileNumher = mobileNumher;
        this.email = email;
        this.type = type;
    }
}

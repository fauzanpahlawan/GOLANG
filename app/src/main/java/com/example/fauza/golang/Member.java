package com.example.fauza.golang;

import com.google.firebase.auth.FirebaseAuth;


public class Member {

    private FirebaseAuth mAuth;

    private String name;
    private String mobileNumber;
    private String email;
    private String password;

    public Member(String name, String mobileNumber, String email, String password) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
    }
}

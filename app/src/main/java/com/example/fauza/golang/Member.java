package com.example.fauza.golang;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Member {

    public String memberName;
    public String mobileNumher;
    public String email;

    public Member() {
    }

    public Member(String memberName, String mobileNumher, String email) {
        this.memberName = memberName;
        this.mobileNumher = mobileNumher;
        this.email = email;

    }
}

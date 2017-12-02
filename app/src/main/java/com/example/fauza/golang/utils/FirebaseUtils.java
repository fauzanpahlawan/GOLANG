package com.example.fauza.golang.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public FirebaseUtils() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public FirebaseDatabase getDb() {
        return mDatabase;
    }

    public DatabaseReference getRef() {
        return mRef;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public FirebaseUser getUser() {
        return mUser;
    }
}

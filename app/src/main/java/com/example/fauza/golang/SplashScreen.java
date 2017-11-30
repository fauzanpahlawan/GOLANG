package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.fauza.golang.activity.HomeMemberActivity;
import com.example.fauza.golang.activity.HomeTourGuideActivity;
import com.example.fauza.golang.activity.LoginActivity;
import com.example.fauza.golang.model.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity implements ValueEventListener {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private Class[] classes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        classes = new Class[3];
        classes[0] = LoginActivity.class;
        classes[1] = HomeTourGuideActivity.class;
        classes[2] = HomeMemberActivity.class;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Query query = mRef.child("members").orderByKey().equalTo(currentUser.getUid());
            query.addValueEventListener(this);
        } else {
            explicitIntent(this, classes[0]);
        }
    }

    private void explicitIntent(Activity loginActivity, Class activity) {
        Intent explicitIntent = new Intent(loginActivity, activity);
        this.startActivity(explicitIntent);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                Member member = postSnapshot.getValue(Member.class);
                if (member != null) {
                    explicitIntent(SplashScreen.this, classes[Integer.valueOf(member.getType())]);
                    Log.i("Login", member.getType());
                }
            }
        } else {
            Log.i("Login", "Data Empty");
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

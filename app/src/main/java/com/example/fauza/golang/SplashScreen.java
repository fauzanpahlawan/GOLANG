package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fauza.golang.activity.HomeMemberActivity;
import com.example.fauza.golang.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            explicitIntent(this, HomeMemberActivity.class);
        }
        //TODO Else statement for TourGuide to HomeTourGuide
        else {
            explicitIntent(this, LoginActivity.class);
        }
    }

    private void explicitIntent(Activity loginActivity, Class activity) {
        Intent explicitIntent = new Intent(loginActivity, activity);
        this.startActivity(explicitIntent);
    }
}

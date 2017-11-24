package com.example.fauza.golang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewCurrentUser;
    private Button buttonLogOut;

    //START check current auth state
    private FirebaseAuth mAuth;
    //END check current auth state

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //START initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        //END initialize FirebaseAuth instance

        textViewCurrentUser = findViewById(R.id.textView_current_user);
        buttonLogOut = findViewById(R.id.button_sign_out);

        FirebaseUser user = mAuth.getCurrentUser();
        String email = "";
        String uid = "";
        if (user != null) {
            email = user.getEmail();
            uid = user.getUid();
        }

        textViewCurrentUser.setText(email + " " + uid + "");
        buttonLogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_out:
                mAuth.signOut();
                explicitIntent();
                break;
        }
    }

    private void explicitIntent() {
        Intent explicitIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(explicitIntent);
    }
}

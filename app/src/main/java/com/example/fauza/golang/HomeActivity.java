package com.example.fauza.golang;

import android.content.Intent;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        textViewCurrentUser = findViewById(R.id.textView_current_user);
        buttonLogOut = findViewById(R.id.button_sign_out);

        setUser();

        buttonLogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_out:
                FirebaseAuth.getInstance().signOut();
                explicitIntent();
                break;
        }
    }

    private void explicitIntent() {
        Intent explicitIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(explicitIntent);
    }

    private void setUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }

        this.textViewCurrentUser.setText(email);
    }
}

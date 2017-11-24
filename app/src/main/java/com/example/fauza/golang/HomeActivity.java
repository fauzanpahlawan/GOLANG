package com.example.fauza.golang;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth mAuth;
    private TextView textViewCurrentUser;
    private Button buttonLogOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        textViewCurrentUser = findViewById(R.id.textView_current_user);
        buttonLogOut = findViewById(R.id.button_sign_out);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                //to do sign out
                break;
        }
    }
}

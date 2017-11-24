package com.example.fauza.golang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName;
    private EditText editTextMobileNumber;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;

    //START check current auth state
    private FirebaseAuth mAuth;
    //END check current auth state

    private String TAG = "EmailPassword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        //START initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        //END initialize FirebaseAuth instance

        editTextName = findViewById(R.id.editText_name);
        editTextMobileNumber = findViewById(R.id.editText_mobile_number);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        buttonSignUp = findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_up:
                if (isEmpty(this.editTextName)) {
                    this.editTextName.setError("Required.");
                } else if (isEmpty(this.editTextMobileNumber)) {
                    this.editTextMobileNumber.setError("Required.");
                } else if (isEmpty(this.editTextEmail)) {
                    this.editTextEmail.setError("Required.");
                } else if (invalidEmail(this.editTextEmail)) {
                    this.editTextEmail.setError("Invalid email.");
                } else if (isEmpty(this.editTextPassword)) {
                    this.editTextPassword.setError("Required.");
                } else {
                    String email = this.editTextEmail.getText().toString();
                    String password = this.editTextPassword.getText().toString();
                    createAccount(email, password);

                }
                break;
        }
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DaftarActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intentHomeActivity = new Intent(DaftarActivity.this, HomeActivity.class);
        startActivity(intentHomeActivity);
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private boolean invalidEmail(EditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches();
    }
}

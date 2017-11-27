package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private Button buttonCreateAnAccount;

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
        buttonCreateAnAccount = findViewById(R.id.button_create_an_account);

        buttonCreateAnAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_an_account:
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
                } else if (lessThanSix(this.editTextPassword)) {
                    this.editTextPassword.setError("At least 6 characters long.");
                } else {
                    this.buttonCreateAnAccount.setClickable(false);
                    this.buttonCreateAnAccount.setText(R.string.create_an_account_progress);
                    String email = this.editTextEmail.getText().toString();
                    String password = this.editTextPassword.getText().toString();
                    createAccount(email, password);
                }
                break;
        }
    }

    /*
    Create Account Starts here
    */

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            explicitIntent(DaftarActivity.this, HomeActivity.class);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DaftarActivity.this, "Create account failed.",
                                    Toast.LENGTH_SHORT).show();
                            buttonCreateAnAccount.setText(R.string.create_an_account);
                            buttonCreateAnAccount.setClickable(true);
                        }
                    }
                });
    }
    /*
    End of Create Account
    */

    private boolean invalidEmail(EditText editText) {
        String email = editText.getText().toString();
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean lessThanSix(EditText editText) {
        String password = editText.getText().toString();
        return password.length() < 6;
    }

    private boolean isEmpty(EditText editText) {
        String text = editText.getText().toString();
        return text.length() == 0;
    }

    private void explicitIntent(Activity loginActivity, Class activity) {
        Intent explicitIntent = new Intent(loginActivity, activity);
        this.startActivity(explicitIntent);
    }
}

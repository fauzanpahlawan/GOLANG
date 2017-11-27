package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout layoutName;
    private TextInputEditText editTextName;
    private TextInputLayout layoutMobileNumber;
    private TextInputEditText editTextMobileNumber;
    private TextInputLayout layoutEmail;
    private TextInputEditText editTextEmail;
    private TextInputLayout layoutPassword;
    private TextInputEditText editTextPassword;
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


        layoutName = findViewById(R.id.layout_name);
        editTextName = findViewById(R.id.editText_name);
        layoutMobileNumber = findViewById(R.id.layout_mobile_number);
        editTextMobileNumber = findViewById(R.id.editText_mobile_number);
        layoutEmail = findViewById(R.id.layout_email);
        editTextEmail = findViewById(R.id.editText_email);
        layoutPassword = findViewById(R.id.layout_password);
        editTextPassword = findViewById(R.id.editText_password);

        buttonCreateAnAccount = findViewById(R.id.button_create_an_account);

        buttonCreateAnAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_an_account:
                if (!isEmpty(editTextName, layoutName)
                        && !isEmpty(editTextMobileNumber, layoutMobileNumber)
                        && !isEmpty(editTextEmail, layoutEmail)
                        && !invalidEmail(editTextEmail, layoutEmail)
                        && !isEmpty(editTextPassword, layoutPassword)
                        && !lessThanSix(editTextPassword, layoutPassword)) {
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

    private boolean invalidEmail(TextInputEditText editText, TextInputLayout textInputLayout) {

        String email = editText.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayout.setError("Invalid email.");
            return true;
        } else {
            textInputLayout.setError(null);
            return false;
        }
    }

    private boolean lessThanSix(TextInputEditText editText, TextInputLayout textInputLayout) {
        String password = editText.getText().toString();
        if (password.length() < 6) {
            textInputLayout.setError("At least 6 characters");
            return true;
        } else {
            textInputLayout.setError(null);
            return false;
        }
    }

    private boolean isEmpty(TextInputEditText editText, TextInputLayout textInputLayout) {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            textInputLayout.setError("*Required");
            return true;
        } else {
            textInputLayout.setError(null);
            return false;
        }
    }

    private void explicitIntent(Activity loginActivity, Class activity) {
        Intent explicitIntent = new Intent(loginActivity, activity);
        this.startActivity(explicitIntent);
    }
}

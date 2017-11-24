
package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgetPassword;
    private TextView textViewSignUp;

    //START check current auth state
    private FirebaseAuth mAuth;
    //END check current auth state

    private String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //START initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        //END initialize FirebaseAuth instance

        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        buttonLogin = findViewById(R.id.button_login);
        textViewForgetPassword = findViewById(R.id.textView_forget_password);
        textViewSignUp = findViewById(R.id.textView_sign_up);

        editTextEmail.addTextChangedListener(this);
        editTextPassword.addTextChangedListener(this);
        buttonLogin.setOnClickListener(this);
        textViewForgetPassword.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
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
            case R.id.button_login:
                if (isEmpty(this.editTextEmail)) {
                    this.editTextEmail.setError("Required.");
                } else if (isEmpty(this.editTextPassword)) {
                    this.editTextPassword.setError("Required.");
                } else {
                    String email = this.editTextEmail.getText().toString();
                    String password = this.editTextPassword.getText().toString();
                    signIn(email, password);
                }
                break;
            case R.id.textView_forget_password:
                //to do forget password
                break;
            case R.id.textView_sign_up:
                //to do intent DaftarActivity
                explicitIntent(LoginActivity.this, DaftarActivity.class);
                break;
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intentHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intentHomeActivity);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //do nothing
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!Patterns.EMAIL_ADDRESS.matcher(this.editTextEmail.getText().toString()).matches()) {
            this.editTextEmail.setError("Please enter a valid Email.");
        }
        if (!isEmpty(this.editTextEmail) && !isEmpty(this.editTextPassword)) {
            this.buttonLogin.setClickable(true);
        }
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private void explicitIntent(Activity loginActivity, Class<DaftarActivity> activity) {
        Intent explicitIntent = new Intent(loginActivity, activity);
        this.startActivity(explicitIntent);
    }
}

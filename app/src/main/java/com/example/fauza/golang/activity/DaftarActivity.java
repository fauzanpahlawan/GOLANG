package com.example.fauza.golang.activity;

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

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DaftarActivity extends AppCompatActivity implements View.OnClickListener, FirebaseAuth.AuthStateListener {
    private TextInputLayout layoutName;
    private TextInputEditText editTextName;
    private TextInputLayout layoutMobileNumber;
    private TextInputEditText editTextMobileNumber;
    private TextInputLayout layoutEmail;
    private TextInputEditText editTextEmail;
    private TextInputLayout layoutPassword;
    private TextInputEditText editTextPassword;
    private Button buttonCreateAnAccount;
    private static final String CHILD_MEMBER = "members";


    /**
     * Firebase Real Time Database Reference
     */
    private DatabaseReference mDatabase;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mAuth;

    private String TAG = "EmailPassword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        // Firebase DatabaseReference instance
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Firebase FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();


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
    protected void onStart() {
        mAuth.addAuthStateListener(this);
        super.onStart();
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (task.getException() != null) {
                                Toast.makeText(DaftarActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            buttonCreateAnAccount.setText(R.string.create_an_account);
                            buttonCreateAnAccount.setClickable(true);
                        }
                    }
                });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String memberName = editTextName.getText().toString();
            String mobileNumber = editTextMobileNumber.getText().toString();
            String email = editTextEmail.getText().toString();
            writeNewMember(user, memberName, mobileNumber, email);
            explicitIntent(DaftarActivity.this, HomeMemberActivity.class);
        }
    }

    private void writeNewMember(FirebaseUser user, String memberName, String mobileNumber, String email) {
        String userUid = user.getUid();
        Member member = new Member(memberName, mobileNumber, email, "2");
        mDatabase.child(CHILD_MEMBER).child(userUid).setValue(member);
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

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        this.startActivity(explicitIntent);
    }
}

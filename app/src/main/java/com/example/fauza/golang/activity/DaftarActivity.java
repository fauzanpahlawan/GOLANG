package com.example.fauza.golang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout layoutDaftarActivity;
    private ImageView imageViewLogo;
    private TextInputLayout layoutName;
    private TextInputEditText editTextName;
    private TextInputLayout layoutMobileNumber;
    private TextInputEditText editTextMobileNumber;
    private TextInputLayout layoutEmail;
    private TextInputEditText editTextEmail;
    private TextInputLayout layoutPassword;
    private TextInputEditText editTextPassword;
    private Toolbar toolbarMain;
    private Button buttonCreateAnAccount;
    private FirebaseAuth.AuthStateListener mAuthListener;


    /**
     * Firebase instance
     */
    FirebaseUtils firebaseUtils = new FirebaseUtils();

    private String TAG = "EmailPassword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);


        layoutDaftarActivity = findViewById(R.id.layout_activity_daftar);
        imageViewLogo = findViewById(R.id.imageView_logo);
        layoutName = findViewById(R.id.layout_name);
        editTextName = findViewById(R.id.editText_name);
        layoutMobileNumber = findViewById(R.id.layout_mobile_number);
        editTextMobileNumber = findViewById(R.id.editText_mobile_number);
        layoutEmail = findViewById(R.id.layout_email);
        editTextEmail = findViewById(R.id.editText_email);
        layoutPassword = findViewById(R.id.layout_password);
        editTextPassword = findViewById(R.id.editText_password);
        toolbarMain = findViewById(R.id.toolbar_home);
        imageViewLogo.setImageResource(R.drawable.logo);

        buttonCreateAnAccount = findViewById(R.id.button_create_an_account);
        buttonCreateAnAccount.setOnClickListener(this);


        setSupportActionBar(toolbarMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DaftarActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_an_account:
                if (!isEmpty(editTextName, layoutName)
                        && !isEmpty(editTextMobileNumber, layoutMobileNumber)
                        && !isNotNumber(editTextMobileNumber, layoutMobileNumber)
                        && !moreThanTwelve(editTextMobileNumber, layoutMobileNumber)
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
        firebaseUtils.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUtils.getAuth().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        String uid = firebaseAuth.getCurrentUser().getUid();
                                        String memberName = editTextName.getText().toString();
                                        String mobileNumber = editTextMobileNumber.getText().toString();
                                        String email = editTextEmail.getText().toString();
                                        writeNewMember(uid, memberName, mobileNumber, email);
                                    }
                                }
                            });
                            Intent intent = new Intent(DaftarActivity.this, HomeMemberActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            DaftarActivity.this.startActivity(intent);
                            DaftarActivity.this.finish();
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (task.getException() != null) {
                                Snackbar snackbar = Snackbar.make(layoutDaftarActivity, task.getException().getMessage(), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                            buttonCreateAnAccount.setText(R.string.create_an_account);
                            buttonCreateAnAccount.setClickable(true);
                        }
                    }
                });
    }

    private void writeNewMember(String uid, String memberName, String mobileNumber, String email) {
        Member member = new Member(
                memberName,
                mobileNumber,
                email,
                getString(R.string.TYPE_MEMBER),
                getString(R.string.photo),
                DaftarActivity.this.getResources().getInteger(R.integer.RATING_POIN_AWAL),
                DaftarActivity.this.getResources().getInteger(R.integer.RATING_VOTER_AWAL));
        firebaseUtils.getRef()
                .child(getString(R.string.members))
                .child(uid)
                .setValue(member);
    }

    /*
    End of Create Account
    */

    private boolean invalidEmail(TextInputEditText editText, TextInputLayout textInputLayout) {
        String email = editText.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Invalid email.");
            return true;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    private boolean isNotNumber(TextInputEditText editText, TextInputLayout textInputLayout) {
        String number = editText.getText().toString();
        if (!TextUtils.isDigitsOnly(number)) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Invalid Phone Number.");
            return true;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    private boolean moreThanTwelve(TextInputEditText editText, TextInputLayout textInputLayout) {
        String number = editText.getText().toString();
        if (number.length() > 12) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Invalid Phone Number.");
            return true;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    private boolean lessThanSix(TextInputEditText editText, TextInputLayout textInputLayout) {
        String password = editText.getText().toString();
        if (password.length() < 6) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("At least 6 characters");
            return true;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return false;
        }

    }

    private boolean isEmpty(TextInputEditText editText, TextInputLayout textInputLayout) {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("*Required");
            return true;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return false;
        }
    }
}

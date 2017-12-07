
package com.example.fauza.golang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout layoutLoginActivity;
    private ImageView imageViewLogo;
    private TextInputLayout layoutEmail;
    private TextInputEditText editTextEmail;
    private TextInputLayout layoutPassword;
    private TextInputEditText editTextPassword;
    private Button buttonSignIn;
    private TextView textViewForgetPassword;
    private TextView textViewSignUp;

    /**
     * Initialize Firebase dataa
     */
    FirebaseUtils firebaseUtils = new FirebaseUtils();
    private Class[] classes;

    private String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        classes = new Class[3];
        classes[1] = HomeTourGuideActivity.class;
        classes[2] = HomeMemberActivity.class;

        layoutLoginActivity = findViewById(R.id.layout_activity_login);
        imageViewLogo = findViewById(R.id.imageView_logo);
        layoutEmail = findViewById(R.id.layout_email);
        editTextEmail = findViewById(R.id.editText_email);
        layoutPassword = findViewById(R.id.layout_password);
        editTextPassword = findViewById(R.id.editText_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        textViewForgetPassword = findViewById(R.id.textView_forget_password);
        textViewForgetPassword.setVisibility(View.INVISIBLE);
        textViewSignUp = findViewById(R.id.textView_sign_up);

        imageViewLogo.setImageResource(R.drawable.logo);

        buttonSignIn.setOnClickListener(this);
        textViewForgetPassword.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_in:
                if (!isEmpty(editTextEmail, layoutEmail)
                        && !isEmpty(editTextPassword, layoutPassword)) {

                    this.buttonSignIn.setClickable(false);
                    this.buttonSignIn.setText(R.string.sign_in_process);
                    String email = this.editTextEmail.getText().toString();
                    String password = this.editTextPassword.getText().toString();
                    signIn(email, password);
                }
                break;
            case R.id.textView_forget_password:
                // TODO Forget Password Event.
                break;
            case R.id.textView_sign_up:
                LoginActivity.this
                        .startActivity(
                                new Intent(LoginActivity.this, DaftarActivity.class)
                        );
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void signIn(String email, String password) {
        firebaseUtils.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUtils.getAuth().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        Query query = firebaseUtils.getRef()
                                                .child(getString(R.string.members))
                                                .child(firebaseAuth.getCurrentUser().getUid());
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Member member = dataSnapshot.getValue(Member.class);
                                                if (member != null) {
                                                    int type = Integer.valueOf(member.getType());
                                                    Intent intent = new Intent(LoginActivity.this, classes[type]);
                                                    LoginActivity.this.startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            });
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if (task.getException() != null) {
                                Snackbar snackbar = Snackbar.make(layoutLoginActivity, "Authentication failed Email or Password is invalid.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            buttonSignIn.setText(R.string.sign_in);
                            buttonSignIn.setClickable(true);
                        }
                    }
                });
    }

    private boolean isEmpty(TextInputEditText editText, TextInputLayout textInputLayout) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            textInputLayout.setError("*Required");
            return true;
        } else {
            textInputLayout.setError(null);
            return false;
        }
    }
}

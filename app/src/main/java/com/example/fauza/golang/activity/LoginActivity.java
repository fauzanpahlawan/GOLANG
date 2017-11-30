
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.SplashScreen;
import com.example.fauza.golang.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        FirebaseAuth.AuthStateListener, ValueEventListener {

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
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    private Class[] classes;

    private String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //START firebase
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        //END firebase

        classes = new Class[3];
        classes[1] = HomeTourGuideActivity.class;
        classes[2] = HomeMemberActivity.class;

        layoutEmail = findViewById(R.id.layout_email);
        editTextEmail = findViewById(R.id.editText_email);
        layoutPassword = findViewById(R.id.layout_password);
        editTextPassword = findViewById(R.id.editText_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        textViewForgetPassword = findViewById(R.id.textView_forget_password);
        textViewSignUp = findViewById(R.id.textView_sign_up);

        buttonSignIn.setOnClickListener(this);
        textViewForgetPassword.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
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
//                            explicitIntent(LoginActivity.this, HomeMemberActivity.class);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Invalid Email or Password",
                                    Toast.LENGTH_SHORT).show();
                            buttonSignIn.setText(R.string.sign_in);
                            buttonSignIn.setClickable(true);
                        }
                    }
                });
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            Query query = mRef.child("members").orderByKey().equalTo(currentUser.getUid());
            query.addValueEventListener(this);
//            explicitIntent(LoginActivity.this, HomeMemberActivity.class);
        }
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

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        this.startActivity(explicitIntent);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                Member member = postSnapshot.getValue(Member.class);
                if (member != null) {
                    explicitIntent(LoginActivity.this, classes[Integer.valueOf(member.getType())]);
                    Log.i("Login", member.getType());
                }
            }
        } else {
            Log.i("Login", "Data Empty");
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

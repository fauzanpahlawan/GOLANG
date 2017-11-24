
package com.example.fauza.golang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgetPassword;
    private TextView textViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                if (isEmpty(this.editTextEmail)) {
                    this.editTextEmail.setError("Required.");
                } else if (isEmpty(this.editTextPassword)) {
                    this.editTextPassword.setError("Required.");
                } else {
                    //to do login
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

    private void explicitIntent(LoginActivity loginActivity, Class<DaftarActivity> activity) {
        Intent explicitIntent = new Intent(loginActivity, activity);
        this.startActivity(explicitIntent);
    }
}

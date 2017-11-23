package com.example.fauza.golang;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName;
    private EditText editTextMobileNumber;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_daftar);

        editTextName = findViewById(R.id.editText_name);
        editTextMobileNumber = findViewById(R.id.editText_mobile_number);
        editTextEmail = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);
        buttonSignUp = findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(this);
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
                    //to do sign up
                }
                break;
        }
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private boolean invalidEmail(EditText editText) {
        return Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches();
    }
}

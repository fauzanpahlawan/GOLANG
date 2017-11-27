package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbarHome = findViewById(R.id.toolbar_home);
        textViewCurrentUser = findViewById(R.id.textView_current_user);

        // Set textView text with the current signed in user
        setUser();

        // Set app logo to account
        toolbarHome.setLogo(R.drawable.ic_account);
        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sign_out:
                FirebaseAuth.getInstance().signOut();
                explicitIntent(this, LoginActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // TODO onClick Handler of various things
        }
    }

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        startActivity(explicitIntent);
    }

    private void setUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }

        this.textViewCurrentUser.setText(email);
    }
}

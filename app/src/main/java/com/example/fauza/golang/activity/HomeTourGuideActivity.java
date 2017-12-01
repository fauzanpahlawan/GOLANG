package com.example.fauza.golang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.fragment.FragmentHomeMember;
import com.example.fauza.golang.fragment.FragmentHomeTourGuide;
import com.example.fauza.golang.utils.FirebaseUtils;

public class HomeTourGuideActivity extends AppCompatActivity {

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private FragmentManager fragmentManager;
    private FragmentHomeTourGuide fragmentHomeTourGuide;
    private FragmentHomeMember fragmentHomeMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tourguide);

        this.fragmentHomeTourGuide = new FragmentHomeTourGuide();
        this.fragmentHomeMember = new FragmentHomeMember();

        this.fragmentManager = this.getSupportFragmentManager();
        this.fragmentManager.beginTransaction()
                .add(R.id.fragment_home_tourguide, this.fragmentHomeTourGuide)
//                .add(R.id.fragment_home_tourguide, this.fragmentHomeMember)
                .commit();

        textViewCurrentUser = findViewById(R.id.textView_current_user);
        toolbarHome = findViewById(R.id.toolbar_home);

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
                firebaseUtils.firebaseAuth().signOut();
                explicitIntent(this, LoginActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        startActivity(explicitIntent);
    }

    private void setUser() {
        if (firebaseUtils.firebaseUser() != null) {
            this.textViewCurrentUser.setText(firebaseUtils.firebaseUser().getEmail());
        }
    }
}

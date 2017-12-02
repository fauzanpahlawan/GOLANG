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
import com.example.fauza.golang.fragment.FragmentHomeMemberRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeMemberActivity extends AppCompatActivity implements ValueEventListener {

    private final String TAG = "HomeMemberActivity";

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private ValueEventListener callBackRequest;
    private Query query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_member);

        textViewCurrentUser = findViewById(R.id.textView_current_user);
        toolbarHome = findViewById(R.id.toolbar_home);

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
    protected void onResume() {
        super.onResume();
        query = firebaseUtils.getRef().child(getString(R.string.TOUR_REQUESTS)).orderByKey().equalTo(firebaseUtils.getUser().getUid());
        final FragmentManager fragmentManager = getSupportFragmentManager();
        callBackRequest = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FragmentHomeMemberRequest fragmentHomeMemberRequest = new FragmentHomeMemberRequest();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_member, fragmentHomeMemberRequest)
                            .commit();
                } else {
                    FragmentHomeMember fragmentHomeMember = new FragmentHomeMember();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_member, fragmentHomeMember)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(callBackRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(callBackRequest);
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
                HomeMemberActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        startActivity(explicitIntent);
    }

    private void setUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.textViewCurrentUser.setText(user.getEmail());
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

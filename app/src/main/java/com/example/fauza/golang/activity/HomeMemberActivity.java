package com.example.fauza.golang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.fragment.FragmentHomeMember;
import com.example.fauza.golang.fragment.FragmentHomeMemberCreateRequest;
import com.example.fauza.golang.model.TourGuideConfirm;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeMemberActivity extends AppCompatActivity implements ValueEventListener {

    private final String TAG = "HomeMemberActivity";

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private ValueEventListener veListener;
    private ChildEventListener ceListener1;
    private ChildEventListener ceListener2;
    private Query query1;
    private Query query2;
    private Query query3;
    private Intent intent;

    FragmentHomeMember fragmentHomeMember;
    FragmentHomeMemberCreateRequest fragmentHomeMemberCreateRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_member);

        textViewCurrentUser = findViewById(R.id.textView_current_user);
        toolbarHome = findViewById(R.id.toolbar_home);

        // Set textView text with the current signed in user
        setUser();

        toolbarHome.setLogo(R.drawable.ic_account);
        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        fragmentHomeMember = new FragmentHomeMember();
        fragmentHomeMemberCreateRequest = new FragmentHomeMemberCreateRequest();
        intent = getIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSupportFragmentManager().beginTransaction()
//                .remove(fragmentHomeMember)
//                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_home_member, new FragmentHomeMember())
                .commit();

        String key = intent.getStringExtra("key");
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
//        query1 = firebaseUtils.getRef()
//                .child(getString(R.string.tourGuideRequests))
//                .orderByChild(firebaseUtils.getUser().getUid());
//        veListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                TourGuideRequest tourGuideRequest = dataSnapshot.getValue(TourGuideRequest.class);
//                Toast.makeText(HomeMemberActivity.this, tourGuideRequest.getTempatWisata(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        query1.addListenerForSingleValueEvent(veListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

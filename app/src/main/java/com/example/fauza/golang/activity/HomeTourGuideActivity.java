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
import com.example.fauza.golang.fragment.FragmentHomeMemberCreateRequest;
import com.example.fauza.golang.fragment.FragmentHomeTourGuide;
import com.example.fauza.golang.fragment.FragmentHomeTourGuideConfirmRequest;
import com.example.fauza.golang.model.TourGuideConfirm;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeTourGuideActivity extends AppCompatActivity {

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private ValueEventListener listener;
    private ChildEventListener cListener;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tourguide);

        textViewCurrentUser = findViewById(R.id.textView_current_user);
        toolbarHome = findViewById(R.id.toolbar_home);

        setUser();
        // Set app logo to account
        toolbarHome.setLogo(R.drawable.ic_account);
        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        FragmentHomeTourGuide fragmentHomeTourGuide = new FragmentHomeTourGuide();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuide)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        query = firebaseUtils.getRef()
                .child(getString(R.string.confirmRequests))
                .orderByChild(getString(R.string.idTourguide))
                .equalTo(firebaseUtils.getUser().getUid());
        final FragmentManager fragmentManager = getSupportFragmentManager();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TourGuideConfirm tourGuideConfirm = dataSnapshot.getValue(TourGuideConfirm.class);
                    FragmentHomeTourGuideConfirmRequest fragmentHomeTourGuideConfirmRequest = new FragmentHomeTourGuideConfirmRequest();
                    Bundle data = new Bundle();
                    data.putString(FragmentHomeTourGuideConfirmRequest.argsKeyTourRequest, tourGuideConfirm.getIdMember());
                    fragmentHomeTourGuideConfirmRequest.setArguments(data);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuideConfirmRequest)
                            .commit();
                } else {
                    FragmentHomeTourGuide fragmentHomeTourGuide = new FragmentHomeTourGuide();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuide)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        cListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    FragmentHomeTourGuideConfirmRequest fragmentHomeTourGuideConfirmRequest = new FragmentHomeTourGuideConfirmRequest();
                    Bundle data = new Bundle();
                    data.putString(FragmentHomeTourGuideConfirmRequest.argsKeyTourRequest, dataSnapshot.getKey());
                    fragmentHomeTourGuideConfirmRequest.setArguments(data);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuideConfirmRequest)
                            .commit();
                } else {
                    FragmentHomeTourGuide fragmentHomeTourGuide = new FragmentHomeTourGuide();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuide)
                            .commit();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TourGuideConfirm tgc = dataSnapshot.getValue(TourGuideConfirm.class);
                if(tgc.getStatus().equals(getString(R.string.CONFIRM_STATUS_COMPLETED))){
                    FragmentHomeTourGuide fragmentHomeTourGuide = new FragmentHomeTourGuide();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuide)
                            .commit();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

//        query.addValueEventListener(listener);
        query.addChildEventListener(cListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(cListener);
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
                firebaseUtils.getAuth().signOut();
                explicitIntent(this, LoginActivity.class);
                HomeTourGuideActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        startActivity(explicitIntent);
    }

    private void setUser() {
        if (firebaseUtils.getUser() != null) {
            this.textViewCurrentUser.setText(firebaseUtils.getUser().getEmail());
        }
    }
}

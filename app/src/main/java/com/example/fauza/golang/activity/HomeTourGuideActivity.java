package com.example.fauza.golang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.fragment.FragmentHomeTourGuide;
import com.example.fauza.golang.fragment.FragmentHomeTourGuideConfirmRequest;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeTourGuideActivity extends AppCompatActivity {

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private ValueEventListener veListener;
    private Query query;

    private FragmentHomeTourGuide fragmentHomeTourGuide;
    private FragmentHomeTourGuideConfirmRequest fragmentHomeTourGuideConfirmRequest;

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        query = firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .orderByChild(getString(R.string.idTourGuide_status))
                .equalTo(firebaseUtils.getUser().getUid() + "_" + getString(R.string.TOUR_STATUS_INPROGRESS))
        ;
        veListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String key = null;
                    TourGuideRequest tourGuideRequest = null;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        key = ds.getKey();
                        tourGuideRequest = ds.getValue(TourGuideRequest.class);
                    }
                    if (tourGuideRequest != null) {
                        fragmentHomeTourGuideConfirmRequest = new FragmentHomeTourGuideConfirmRequest();
                        Bundle data = new Bundle();
                        data.putString(FragmentHomeTourGuideConfirmRequest.argsKeyTourGuideRequest, key);
                        data.putString(FragmentHomeTourGuideConfirmRequest.argsIdMember, tourGuideRequest.getIdMember());
                        fragmentHomeTourGuideConfirmRequest.setArguments(data);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuideConfirmRequest)
                                .commit();
                    }

                } else {
                    fragmentHomeTourGuide = new FragmentHomeTourGuide();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_home_tourguide, fragmentHomeTourGuide)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(veListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(veListener);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeTourGuideActivity.this);
                alertDialog.setMessage("Yakin akan sign out?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseUtils.getAuth().signOut();
                        Intent intent = new Intent(HomeTourGuideActivity.this, LoginActivity.class);
                        HomeTourGuideActivity.this.startActivity(intent);
                        HomeTourGuideActivity.this.finish();
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do Nothing
                    }
                });
                AlertDialog signOutAlert = alertDialog.create();
                signOutAlert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUser() {
        if (firebaseUtils.getUser() != null) {
            this.textViewCurrentUser.setText(firebaseUtils.getUser().getEmail());
        }
    }
}

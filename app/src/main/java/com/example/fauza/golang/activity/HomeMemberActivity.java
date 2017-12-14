package com.example.fauza.golang.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.fragment.FragmentGiveRating;
import com.example.fauza.golang.fragment.FragmentHomeMember;
import com.example.fauza.golang.fragment.FragmentHomeMemberCreateRequest;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeMemberActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private final String TAG = "HomeMemberActivity";

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private ValueEventListener veListener;
    private Query query1;

    FragmentHomeMember fragmentHomeMember;
    FragmentHomeMemberCreateRequest fragmentHomeMemberCreateRequest;
    FragmentGiveRating fragmentGiveRating;


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

        firebaseUtils.getAuth().addAuthStateListener(this);
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
        query1 = firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .orderByChild(getString(R.string.idMember_status))
                .equalTo(firebaseUtils.getUser().getUid() + "_" + getString(R.string.TOUR_STATUS_INPROGRESS));
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
                        fragmentHomeMemberCreateRequest = new FragmentHomeMemberCreateRequest();
                        Bundle data = new Bundle();
                        data.putString(FragmentHomeMemberCreateRequest.argsKeyTourGuideRequest, key);

                        data.putString(FragmentHomeMemberCreateRequest.argsIdTourGuide, tourGuideRequest.getIdTourGuide());
                        fragmentHomeMemberCreateRequest.setArguments(data);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_home_member, fragmentHomeMemberCreateRequest)
                                .commit();
                        if (tourGuideRequest.getRequestStatus() == HomeMemberActivity.this.getResources().getInteger(R.integer.TOUR_STATUS_COMPLETED)) {
                            fragmentGiveRating = new FragmentGiveRating();
                            data.putString(FragmentGiveRating.argsKeyTourGuideRequests, key);
                            data.putString(FragmentGiveRating.argsIdTourGuide, tourGuideRequest.getIdTourGuide());
                            fragmentGiveRating.setArguments(data);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_home_member, fragmentGiveRating)
                                    .commit();
                        }
                    }
                } else {
                    fragmentHomeMember = new FragmentHomeMember();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_home_member, fragmentHomeMember)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query1.addValueEventListener(veListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query1.removeEventListener(veListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseUtils.getAuth().removeAuthStateListener(this);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeMemberActivity.this);
                alertDialog.setMessage("Yakin akan sign out?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseUtils.getAuth().signOut();
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

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(HomeMemberActivity.this, LoginActivity.class);
            HomeMemberActivity.this.startActivity(intent);
            HomeMemberActivity.this.finish();
        }
    }

    private void setUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.textViewCurrentUser.setText(user.getEmail());
        }
    }
}

package com.example.fauza.golang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.model.TourGuideConfirm;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailTourGuideRequestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewNamaMember;
    private TextView textViewNamaTempat;
    private TextView textViewJumlahWisatawan;
    private TextView textViewTanggalWisata;
    private ImageButton imageButtonMessage;
    private ImageButton imageButtonCall;
    private Button buttonTerimaRequest;
    private Toolbar toolbarMain;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private Query query;
    private ValueEventListener callBack;
    private String idTourRequest;
    private String idMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour_guide_request);

        textViewNamaMember = findViewById(R.id.tv_nama_member);
        textViewNamaTempat = findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = findViewById(R.id.tv_tanggal_wisata);
        imageButtonMessage = findViewById(R.id.iBt_message);
        imageButtonCall = findViewById(R.id.iBt_call);
        buttonTerimaRequest = findViewById(R.id.bt_terima_request);
        toolbarMain = findViewById(R.id.toolbar_main);

        Intent intent = getIntent();
        idTourRequest = intent.getStringExtra(getString(R.string.KEY_TOUR_GUIDE_REQUEST));
        idMember = intent.getStringExtra(getString(R.string.idMember));
        Log.i("ID_TOUR", idTourRequest);
        query = firebaseUtils.getRef()
                .child(getString(R.string.members))
                .child(idMember);
        callBack = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                if (member != null) {
                    textViewNamaMember.setText(member.getMemberName());
                    imageButtonMessage.setTag(member.getMobileNumher());
                    imageButtonCall.setTag(member.getMobileNumher());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(callBack);

        query = firebaseUtils.getRef().child(getString(R.string.tourGuideRequests)).child(idTourRequest);
        callBack = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TourGuideRequest tourGuideRequest = dataSnapshot.getValue(TourGuideRequest.class);
                if (tourGuideRequest != null) {
                    textViewNamaTempat.setText(tourGuideRequest.getTempatWisata());
                    textViewJumlahWisatawan.setText(tourGuideRequest.getJumlahWisatawan());
                    textViewTanggalWisata.setText(tourGuideRequest.getTanggalWisata());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        query.addListenerForSingleValueEvent(callBack);


        imageButtonMessage.setOnClickListener(this);
        imageButtonCall.setOnClickListener(this);
        buttonTerimaRequest.setOnClickListener(this);

        setSupportActionBar(toolbarMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBt_message:
                Toast.makeText(
                        DetailTourGuideRequestActivity.this,
                        imageButtonMessage.getTag().toString(),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.iBt_call:
                Toast.makeText(
                        DetailTourGuideRequestActivity.this,
                        imageButtonCall.getTag().toString(),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_terima_request:
                TourGuideConfirm tourGuideConfirm = new TourGuideConfirm(
                        idTourRequest,
                        firebaseUtils.getUser().getUid(),
                        idMember,
                        firebaseUtils.getUser().getDisplayName(),
                        getString(R.string.CONFIRM_STATUS_ONGOING));
                firebaseUtils.getRef()
                        .child(getString(R.string.confirmRequests))
                        .push()
                        .setValue(tourGuideConfirm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("DetailTourRequest", "ConfirmSuccess");
                                    DetailTourGuideRequestActivity.this.finish();
                                } else {
                                    Log.w("DetailTourRequest", task.getException());
                                }
                            }
                        });
                firebaseUtils.getRef()
                        .child(getString(R.string.tourGuideRequests))
                        .child(idTourRequest)
                        .child(getString(R.string.REQUEST_STATUS))
                        .setValue(firebaseUtils.getUser().getDisplayName());
                break;
        }
    }
}

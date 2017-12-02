package com.example.fauza.golang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class DetailTourRequestActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tour_request);

        textViewNamaMember = findViewById(R.id.tv_nama_member);
        textViewNamaTempat = findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = findViewById(R.id.tv_tanggal_wisata);
        imageButtonMessage = findViewById(R.id.iBt_message);
        imageButtonCall = findViewById(R.id.iBt_call);
        buttonTerimaRequest = findViewById(R.id.bt_terima_request);
        toolbarMain = findViewById(R.id.toolbar_main);

        Intent intent = getIntent();
        idTourRequest = intent.getStringExtra(getString(R.string.KEY_TOUR_REQUEST));
        Log.i("ID_TOUR", idTourRequest);
        query = firebaseUtils.getRef().child(getString(R.string.MEMBERS)).orderByKey().equalTo(idTourRequest);
        callBack = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    member = ds.getValue(Member.class);
                }
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

        query = firebaseUtils.getRef().child(getString(R.string.TOUR_REQUESTS)).orderByKey();
        callBack = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TourGuideRequest tourGuideRequest = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tourGuideRequest = ds.getValue(TourGuideRequest.class);
                }
                if (tourGuideRequest != null) {
                    textViewNamaTempat.setText(tourGuideRequest.getTujuanWisata());
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
                        DetailTourRequestActivity.this,
                        imageButtonMessage.getTag().toString(),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.iBt_call:
                Toast.makeText(
                        DetailTourRequestActivity.this,
                        imageButtonCall.getTag().toString(),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_terima_request:
                TourGuideConfirm tourGuideConfirm = new TourGuideConfirm(
                        firebaseUtils.getUser().getUid(),
                        firebaseUtils.getUser().getDisplayName()
                );
                firebaseUtils.getRef()
                        .child(getString(R.string.CONFIRM_REQUEST))
                        .child(idTourRequest)
                        .setValue(tourGuideConfirm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("DetailTourRequest", "ConfirmSuccess");
                                    DetailTourRequestActivity.this.finish();
                                } else {
                                    Log.w("DetailTourRequest", task.getException());
                                }
                            }
                        });
                break;
        }
    }
}

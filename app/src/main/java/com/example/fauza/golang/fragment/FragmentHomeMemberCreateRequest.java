package com.example.fauza.golang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.TourGuideConfirm;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHomeMemberCreateRequest extends Fragment implements View.OnClickListener {
    public static final String argsKeyTourGuideRequest = "tourGuideRequest";
    public static final String argsIdTourGuide = "idTourGuide";
    public View view;
    public TextView textViewNamaTempat;
    public TextView textViewJumlahWisatawan;
    public TextView textViewTanggalWisata;
    public TextView textViewStatus;
    public Button buttonCancelRequest;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_home_member_create_request,
                container,
                false);

        textViewNamaTempat = view.findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = view.findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = view.findViewById(R.id.tv_tanggal_wisata);
        textViewStatus = view.findViewById(R.id.tv_status_request);
        buttonCancelRequest = view.findViewById(R.id.bt_cancel_request);

        buttonCancelRequest.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequest);
        Query query1 = firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .child(keyTourGuideRequest);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TourGuideRequest tourGuideRequest = dataSnapshot.getValue(TourGuideRequest.class);
                if (tourGuideRequest != null) {
                    textViewNamaTempat.setText(tourGuideRequest.getTempatWisata());
                    textViewJumlahWisatawan.setText(tourGuideRequest.getJumlahWisatawan());
                    textViewTanggalWisata.setText(tourGuideRequest.getTanggalWisata());
                }
                textViewStatus.setText(tourGuideRequest.getStatus());
                if (!tourGuideRequest.getStatus().equals(getString(R.string.BELUM_ADA_TOUR_GUIDE))) {
                    buttonCancelRequest.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel_request:
                String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequest);
                firebaseUtils.getRef()
                        .child(getString(R.string.tourGuideRequests))
                        .child(keyTourGuideRequest)
                        .removeValue();
                break;
        }
    }
}

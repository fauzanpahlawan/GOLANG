package com.example.fauza.golang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FragmentHomeTourGuideConfirmRequest extends Fragment implements View.OnClickListener {
    public static final String argsKeyConfirmRequest = "confirmRequests";
    public static final String argsIdTourGuideRequest = "tourGuideRequest";
    public static final String argsIdMember = "idMember";
    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    public View view;
    public TextView textViewNamaMember;
    public TextView textViewNamaTempat;
    public TextView textViewJumlahWisatawan;
    public TextView textViewTanggalWisata;
    public Button buttonCompleteRequest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_home_tourguide_confirm_request,
                container,
                false);


        textViewNamaMember = view.findViewById(R.id.tv_nama_member);
        textViewNamaTempat = view.findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = view.findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = view.findViewById(R.id.tv_tanggal_wisata);
        buttonCompleteRequest = view.findViewById(R.id.bt_selesai_tour);

        buttonCompleteRequest.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String keyTourGuideRequests = getArguments().getString(argsIdTourGuideRequest);
        String idMember = getArguments().getString(argsIdMember);
        Query query1 = firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .child(keyTourGuideRequests);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TourGuideRequest tourGuideRequest = dataSnapshot.getValue(TourGuideRequest.class);
                if (tourGuideRequest != null) {
                    textViewNamaTempat.setText(tourGuideRequest.getTempatWisata());
                    textViewTanggalWisata.setText(tourGuideRequest.getTanggalWisata());
                    textViewJumlahWisatawan.setText(tourGuideRequest.getJumlahWisatawan());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Query query2 = firebaseUtils.getRef()
                .child(getString(R.string.members))
                .child(idMember);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                textViewNamaMember.setText(member.getMemberName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_selesai_tour:
                String keyConfirmRequest = getArguments().getString(argsKeyConfirmRequest);
                String idTourGuideRequest = getArguments().getString(argsIdTourGuideRequest);
                firebaseUtils.getRef()
                        .child(getString(R.string.confirmRequests))
                        .child(keyConfirmRequest)
                        .child(getString(R.string.REQUEST_STATUS))
                        .setValue(getString(R.string.CONFIRM_STATUS_COMPLETED));
                firebaseUtils.getRef()
                        .child(getString(R.string.tourGuideRequests))
                        .child(idTourGuideRequest)
                        .child(getString(R.string.REQUEST_STATUS))
                        .setValue(FragmentHomeTourGuideConfirmRequest.this.getResources().getInteger(R.integer.TOUR_STATUS_COMPLETED));

                break;
        }
    }
}

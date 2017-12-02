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
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHomeMemberRequest extends Fragment implements View.OnClickListener {
    public View view;
    public ArrayList<TextView> textViews = new ArrayList<>();
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
                R.layout.fragment_home_member_request,
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
        Query query1 = firebaseUtils.getRef()
                .child(getString(R.string.TOUR_REQUESTS))
                .orderByKey()
                .equalTo(firebaseUtils.getUser().getUid());
        final ArrayList<String> data = new ArrayList<>();
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TourGuideRequest tourGuideRequest = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tourGuideRequest = ds.getValue(TourGuideRequest.class);
                }
                if (tourGuideRequest != null) {
                    data.add(tourGuideRequest.getTujuanWisata());
                    data.add(tourGuideRequest.getJumlahWisatawan());
                    data.add(tourGuideRequest.getTanggalWisata());
                    data.add(tourGuideRequest.getStatus());
                }

                textViewNamaTempat.setText(data.get(0));
                textViewJumlahWisatawan.setText(data.get(1));
                textViewTanggalWisata.setText(data.get(2));
                textViewStatus.setText(data.get(3));

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
                firebaseUtils.getRef()
                        .child(getString(R.string.TOUR_REQUESTS))
                        .child(firebaseUtils.getUser().getUid())
                        .removeValue();
                break;
        }
    }
}

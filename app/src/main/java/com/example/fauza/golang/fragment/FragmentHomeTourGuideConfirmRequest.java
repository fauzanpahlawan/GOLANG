package com.example.fauza.golang.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.activity.DetailTourGuideRequestActivity;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FragmentHomeTourGuideConfirmRequest extends Fragment implements View.OnClickListener {
    public static final String argsKeyTourGuideRequest = "keyTourGuideRequest";
    public static final String argsIdMember = "idMember";
    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    public View view;
    public TextView textViewNamaMember;
    public ImageButton imageButtonCall;
    public ImageButton imageButtonMessage;
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
        imageButtonCall = view.findViewById(R.id.iBt_call);
        imageButtonMessage = view.findViewById(R.id.iBt_message);
        textViewNamaTempat = view.findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = view.findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = view.findViewById(R.id.tv_tanggal_wisata);
        buttonCompleteRequest = view.findViewById(R.id.bt_selesai_tour);

        imageButtonCall.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        buttonCompleteRequest.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequest);
        String idMember = getArguments().getString(argsIdMember);
        if (keyTourGuideRequest != null) {
            Query query1 = firebaseUtils.getRef()
                    .child(getString(R.string.tourGuideRequests))
                    .child(keyTourGuideRequest);
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
        }
        if (idMember != null) {
            Query query2 = firebaseUtils.getRef()
                    .child(getString(R.string.members))
                    .child(idMember);
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Member member = dataSnapshot.getValue(Member.class);
                    if (member != null) {
                        textViewNamaMember.setText(member.getMemberName());
                        imageButtonCall.setTag(member.getMobileNumher());
                        imageButtonMessage.setTag(member.getMobileNumher());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_selesai_tour:
                //COMPLETED Prompt Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(FragmentHomeTourGuideConfirmRequest.this.getActivity());
                builder.setMessage(R.string.alert_message_tourguide);
                builder.setTitle("Menyelesaikan Tour ini ?");
                builder.setPositiveButton(R.string.selesai, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequest);
                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put(getString(R.string.idTourGuide_status), firebaseUtils.getUser().getUid() + "_" + getString(R.string.TOUR_STATUS_COMPLETED));
                        updateMap.put(getString(R.string.requestStatus), FragmentHomeTourGuideConfirmRequest.this.getResources().getInteger(R.integer.TOUR_STATUS_COMPLETED));
                        if (keyTourGuideRequest != null) {
                            firebaseUtils.getRef()
                                    .child(getString(R.string.tourGuideRequests))
                                    .child(keyTourGuideRequest)
                                    .updateChildren(updateMap);
                        }
                    }
                });
                builder.setNegativeButton(R.string.belum_selesai, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do Nothing
                    }
                });
                builder.show();
                break;
            case R.id.iBt_message:
                //COMPLETED Intent Message to Number
                String phoneMsg = imageButtonMessage.getTag().toString();
                Intent intentMsg = new Intent(Intent.ACTION_SENDTO);
                intentMsg.setData(Uri.parse("smsto:" + phoneMsg));
                if (intentMsg.resolveActivity(FragmentHomeTourGuideConfirmRequest.this.getActivity().getPackageManager()) != null) {
                    FragmentHomeTourGuideConfirmRequest.this.startActivity(intentMsg);
                }
                break;
            case R.id.iBt_call:
                //COMPLETED Intent Call to Number
                String phoneCall = imageButtonCall.getTag().toString();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + phoneCall));
                if (intentCall.resolveActivity(FragmentHomeTourGuideConfirmRequest.this.getActivity().getPackageManager()) != null) {
                    FragmentHomeTourGuideConfirmRequest.this.startActivity(intentCall);
                }
                break;
        }
    }
}

package com.example.fauza.golang.fragment;

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

import com.example.fauza.golang.R;
import com.example.fauza.golang.activity.DetailTourGuideRequestActivity;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FragmentHomeMemberCreateRequest extends Fragment implements View.OnClickListener {
    public static final String argsKeyTourGuideRequest = "tourGuideRequest";
    public static final String argsIdTourGuide = "idTourGuide";

    public View view;
    public TextView textViewNamaTempat;
    public TextView textViewJumlahWisatawan;
    public TextView textViewTanggalWisata;
    public TextView textViewStatus;
    public ImageView imageViewPhotoTourGuide;
    public TextView textViewNamaTourGuide;
    public TextView textViewRatingTourGuide;
    public ImageButton imageButtonCall;
    public ImageButton imageButtonMessage;
    public Button buttonCancelRequest;


    public Query query1;
    public Query query2;
    public ValueEventListener veListener1;
    public ValueEventListener veListener2;

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
        imageViewPhotoTourGuide = view.findViewById(R.id.imageView_tour_photo_guide);
        textViewNamaTourGuide = view.findViewById(R.id.tv_nama_tour_guide);
        textViewRatingTourGuide = view.findViewById(R.id.tv_rating_tour_guide);
        imageButtonCall = view.findViewById(R.id.iBt_call);
        imageButtonMessage = view.findViewById(R.id.iBt_message);


        buttonCancelRequest.setVisibility(View.INVISIBLE);

        imageButtonCall.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        buttonCancelRequest.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequest);
        String idTourGuide = getArguments().getString(argsIdTourGuide);
        if (keyTourGuideRequest != null) {
            query1 = firebaseUtils.getRef()
                    .child(getString(R.string.tourGuideRequests))
                    .child(keyTourGuideRequest);
            veListener1 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TourGuideRequest tourGuideRequest = dataSnapshot.getValue(TourGuideRequest.class);
                    if (tourGuideRequest != null) {
                        textViewNamaTempat.setText(tourGuideRequest.getTempatWisata());
                        textViewJumlahWisatawan.setText(tourGuideRequest.getJumlahWisatawan());
                        textViewTanggalWisata.setText(tourGuideRequest.getTanggalWisata());
                        if (tourGuideRequest.getRequestStatus() == FragmentHomeMemberCreateRequest.this.getResources().getInteger(R.integer.TOUR_STATUS_CREATED)) {
                            textViewStatus.setText(getString(R.string.MENCARI_TOURGUIDE));
                            buttonCancelRequest.setVisibility(View.VISIBLE);
                        } else {
                            buttonCancelRequest.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }


        if (idTourGuide != null) {
            query2 = firebaseUtils.getRef()
                    .child(getString(R.string.members))
                    .child(idTourGuide);
            veListener2 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Member member = dataSnapshot.getValue(Member.class);
                    if (member != null) {
                        textViewStatus.setText(getString(R.string.MENEMUKAN_TOURGUIDE));
                        //COMPLETED SET PHOTO TOURGUIDE
                        Uri uri = Uri.parse(member.getPhoto());
                        Picasso.with(FragmentHomeMemberCreateRequest.this.getActivity()).load(uri).into(imageViewPhotoTourGuide);
                        textViewNamaTourGuide.setText(member.getMemberName());
                        String ratingPlaceHolder = "Rating: " + String.valueOf(
                                calculateRating(member.getRatingPoin(), member.getRatingVoter())
                        );
                        textViewRatingTourGuide.setText(ratingPlaceHolder);
                        imageButtonCall.setTag(member.getMobileNumher());
                        imageButtonMessage.setTag(member.getMobileNumher());
                        imageButtonCall.setVisibility(View.VISIBLE);
                        imageButtonMessage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        query1.addValueEventListener(veListener1);
        query2.addValueEventListener(veListener2);

    }

    private float calculateRating(float ratingPoin, float ratingVoter) {
        return ratingPoin / ratingVoter;
    }

    @Override
    public void onStop() {
        super.onStop();
        query1.removeEventListener(veListener1);
        query2.removeEventListener(veListener2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel_request:
                String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequest);
                if (keyTourGuideRequest != null) {
                    firebaseUtils.getRef()
                            .child(getString(R.string.tourGuideRequests))
                            .child(keyTourGuideRequest)
                            .removeValue();
                }
                break;
            case R.id.iBt_message:
                String phoneMsg = imageButtonMessage.getTag().toString();
                Intent intentMsg = new Intent(Intent.ACTION_SENDTO);
                intentMsg.setData(Uri.parse("smsto:" + phoneMsg));
                if (intentMsg.resolveActivity(FragmentHomeMemberCreateRequest.this.getActivity().getPackageManager()) != null) {
                    FragmentHomeMemberCreateRequest.this.startActivity(intentMsg);
                }
                break;
            case R.id.iBt_call:
                String phoneCall = imageButtonCall.getTag().toString();
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + phoneCall));
                if (intentCall.resolveActivity(FragmentHomeMemberCreateRequest.this.getActivity().getPackageManager()) != null) {
                    FragmentHomeMemberCreateRequest.this.startActivity(intentCall);
                }
                break;
        }
    }
}

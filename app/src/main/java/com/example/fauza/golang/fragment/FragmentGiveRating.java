package com.example.fauza.golang.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.Member;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;

public class FragmentGiveRating extends Fragment {
    public static final String argsKeyTourGuideRequests = "keyTourGuideRequest";
    public static final String argsIdTourGuide = "idTourGuide";

    public ImageView imageViewPhotoTourGuide;
    private TextView textViewNamaTourGuide;
    private TextView textViewRatingTourGuide;
    private RatingBar ratingBarTourGuide;
    private TextView textViewRating;
    private Button buttonSubmitRating;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private float lastPoint;
    private float lastVoter;
    private float ratingPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_give_rating,
                container,
                false);

        imageViewPhotoTourGuide = view.findViewById(R.id.imageView_photo_tour_guide);
        textViewNamaTourGuide = view.findViewById(R.id.tv_nama_tour_guide);
        textViewRatingTourGuide = view.findViewById(R.id.tv_rating_tour_guide);
        ratingBarTourGuide = view.findViewById(R.id.rb_tour_guide);
        textViewRating = view.findViewById(R.id.tv_rating);
        buttonSubmitRating = view.findViewById(R.id.bt_submit_rating);

        String idTourGuide = getArguments().getString(argsIdTourGuide);
        if (idTourGuide != null) {
            Query query = firebaseUtils.getRef()
                    .child(getString(R.string.members))
                    .child(idTourGuide);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Member member = dataSnapshot.getValue(Member.class);
                    String placeHolderNama = null;
                    String placeHolderRating = null;
                    float rating = 0;
                    if (member != null) {
                        lastPoint = member.getRatingPoin();
                        lastVoter = member.getRatingVoter();
                        rating = lastPoint / lastVoter;
                        Uri uri = Uri.parse(member.getPhoto());
                        Picasso.with(FragmentGiveRating.this.getActivity()).load(uri).into(imageViewPhotoTourGuide);
                        placeHolderNama = String.format(Locale.ENGLISH, "%s", member.getMemberName());
                        placeHolderRating = String.format(Locale.ENGLISH, "%s %.1f", "Rating: ", rating);
                    }
                    textViewNamaTourGuide.setText(placeHolderNama);
                    textViewRatingTourGuide.setText(placeHolderRating);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        ratingBarTourGuide.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingPoint = v;
                String placeHolderRating = String.format(Locale.ENGLISH, "%s %.1f", "Anda beri: ", v);
                textViewRating.setText(placeHolderRating);

            }
        });

        buttonSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FragmentGiveRating.this.getActivity());
                builder.setMessage("Terimakasih atas partisipasi anda.");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequests);
                        HashMap<String, Object> updateRequest = new HashMap<>();
                        updateRequest.put(getString(R.string.idMember_status), firebaseUtils.getUser().getUid() + "_" + getString(R.string.TOUR_STATUS_COMPLETED));
                        updateRequest.put(getString(R.string.requestStatus), FragmentGiveRating.this.getResources().getInteger(R.integer.TOUR_STATUS_RATED));
                        if (keyTourGuideRequest != null) {
                            firebaseUtils.getRef()
                                    .child(getString(R.string.tourGuideRequests))
                                    .child(keyTourGuideRequest)
                                    .updateChildren(updateRequest);
                        }
                        float newRatingPoint = lastPoint + ratingPoint;
                        float newRatingVoter = ++lastVoter;
                        String idTourGuide = getArguments().getString(argsIdTourGuide);
                        HashMap<String, Object> updateTourGuide = new HashMap<>();
                        updateTourGuide.put(getString(R.string.ratingPoin), newRatingPoint);
                        updateTourGuide.put(getString(R.string.ratingVoter), newRatingVoter);
                        if (idTourGuide != null) {
                            firebaseUtils.getRef()
                                    .child(getString(R.string.members))
                                    .child(idTourGuide)
                                    .updateChildren(updateTourGuide);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }


}

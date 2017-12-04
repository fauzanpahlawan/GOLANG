package com.example.fauza.golang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.utils.FirebaseUtils;

public class FragmentGiveRating extends Fragment {
    public static final String argsKeyTourGuideRequests = "keyTourGuideRequest";

    private TextView textViewGiveRating;
    private FirebaseUtils firebaseUtils = new FirebaseUtils();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_give_rating,
                container,
                false);

        textViewGiveRating = view.findViewById(R.id.tv_give_rating);
        textViewGiveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyTourGuideRequest = getArguments().getString(argsKeyTourGuideRequests);
                firebaseUtils.getRef()
                        .child(getString(R.string.tourGuideRequests))
                        .child(keyTourGuideRequest)
                        .child(getString(R.string.REQUEST_STATUS))
                        .setValue(FragmentGiveRating.this.getResources().getInteger(R.integer.TOUR_STATUS_RATED));
            }
        });
        return view;
    }
}

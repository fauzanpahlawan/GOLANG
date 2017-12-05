package com.example.fauza.golang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.adapter.ListRequestAdapter;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

public class FragmentHomeTourGuide extends Fragment {

    private View view;
    private TextView textView;

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private RecyclerView recyclerViewListRequest;
    private ListRequestAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_home_tourguide, container, false);
        this.textView = this.view.findViewById(R.id.tv_list_request);
        recyclerViewListRequest = this.view.findViewById(R.id.rv_request_list);

        Query query = firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .orderByChild(getString(R.string.requestStatus))
                .equalTo(FragmentHomeTourGuide.this.getResources().getInteger(R.integer.TOUR_STATUS_CREATED));
        FirebaseRecyclerOptions<TourGuideRequest> options =
                new FirebaseRecyclerOptions.Builder<TourGuideRequest>()
                        .setQuery(query, TourGuideRequest.class)
                        .build();

        recyclerViewListRequest.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ListRequestAdapter(options, getActivity());
        recyclerViewListRequest.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        return this.view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

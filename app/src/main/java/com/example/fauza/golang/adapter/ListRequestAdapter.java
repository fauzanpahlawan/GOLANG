package com.example.fauza.golang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.activity.DetailTourGuideRequestActivity;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.viewHolder.ListRequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;


public class ListRequestAdapter extends FirebaseRecyclerAdapter<TourGuideRequest, ListRequestViewHolder> {

    private Context mContext;
    private final ObservableSnapshotArray<TourGuideRequest> mSnapshots;


    public ListRequestAdapter(FirebaseRecyclerOptions<TourGuideRequest> options, Context mContext) {
        super(options);
        mSnapshots = options.getSnapshots();
        this.mContext = mContext;
    }

    @Override
    public ListRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_tour_guide_request, parent, false);
        return new ListRequestViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ListRequestViewHolder holder, int position, final TourGuideRequest model) {
        final int itemPosition = position;
        holder.textViewTempatWisata.setText(model.getTempatWisata());
        holder.textViewJumlahWisatawan.setText(model.getJumlahWisatawan());
        holder.textViewTanggalWisata.setText(model.getTanggalWisata());
        holder.linearLayoutTourRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailTourGuideRequestActivity.class);
                intent.putExtra(mContext.getString(R.string.KEY_TOUR_GUIDE_REQUEST), getRef(itemPosition).getKey());
                intent.putExtra(mContext.getString(R.string.idMember), model.getIdMember());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSnapshots.isListening(this) ? mSnapshots.size() : 0;
    }
}

package com.example.fauza.golang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.activity.DetailTourRequestActivity;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.viewHolder.ListRequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ListRequestAdapter extends FirebaseRecyclerAdapter<TourGuideRequest, ListRequestViewHolder> {

    private Context mContext;

    public ListRequestAdapter(FirebaseRecyclerOptions<TourGuideRequest> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    public ListRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_list_request, parent, false);
        return new ListRequestViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ListRequestViewHolder holder, final int position, final TourGuideRequest model) {
        holder.textViewTempatWisata.setText(model.getTujuanWisata());
        holder.textViewJumlahWisatawan.setText(model.getJumlahWisatawan());
        holder.textViewTanggalWisata.setText(model.getTanggalWisata());
        holder.linearLayoutTourRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Anda Memilih Tour Request Ke " + model.getTujuanWisata(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailTourRequestActivity.class);
                intent.putExtra(mContext.getString(R.string.KEY_TOUR_REQUEST), getRef(position).getKey());
                mContext.startActivity(intent);
            }
        });
    }
}

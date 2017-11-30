package com.example.fauza.golang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fauza.golang.R;
import com.example.fauza.golang.viewHolder.TempatWisataViewHolder;
import com.example.fauza.golang.activity.RequestTourActivity;

import java.util.ArrayList;
import java.util.List;


public class TempatWisataAdapter extends RecyclerView.Adapter<TempatWisataViewHolder> {
    private Context mContext;
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> dataCopy = new ArrayList<>();

    public TempatWisataAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<String> data) {
        this.data.addAll(data);
        this.dataCopy.addAll(data);
    }

    @Override
    public TempatWisataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_tempat_wisata, parent, false);
        return new TempatWisataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TempatWisataViewHolder holder, int position) {
        holder.textViewNamaTempat.setText(data.get(position));
        final int dataPosition = position;
        holder.buttonPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RequestTourActivity.class);
                intent.putExtra("nama_tempat", data.get(dataPosition));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void filter(String text) {
        this.data.clear();
        if (text.isEmpty()) {
            this.data.addAll(dataCopy);
        } else {
            text = text.toLowerCase();
            for (int i = 0; i < this.dataCopy.size(); i++) {
                if (this.dataCopy.get(i).toLowerCase().contains(text.toLowerCase())) {
                    this.data.add(dataCopy.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}

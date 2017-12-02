package com.example.fauza.golang.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fauza.golang.R;

public class ListRequestViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewTempatWisata;
    public TextView textViewJumlahWisatawan;
    public TextView textViewTanggalWisata;
    public LinearLayout linearLayoutTourRequest;

    public ListRequestViewHolder(View itemView) {
        super(itemView);
        textViewTempatWisata = itemView.findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = itemView.findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = itemView.findViewById(R.id.tv_tanggal_wisata);
        linearLayoutTourRequest = itemView.findViewById(R.id.layout_tour_request);
    }
}

package com.example.fauza.golang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fauza.golang.R;

public class FragmentHomeMemberRequest extends Fragment {
    public View view;
    public TextView textViewNamaTempat;
    public TextView textViewJumlahWisatawan;
    public TextView textViewTanggalWisata;
    public TextView textViewStatus;

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

        return view;
    }
}

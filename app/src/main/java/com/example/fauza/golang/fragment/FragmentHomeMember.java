package com.example.fauza.golang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.fauza.golang.R;
import com.example.fauza.golang.adapter.TempatWisataAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FragmentHomeMember extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    private View view;
    private SearchView searchViewTujuanWisata;
    private List<String> data;
    private TempatWisataAdapter mAdapter;
    private RecyclerView rvTempatWisata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(
                R.layout.fragment_home_member,
                container,
                false);
        data = Arrays.asList(getResources().getStringArray(R.array.tempat_wisata));
        Collections.sort(data);

        searchViewTujuanWisata = this.view.findViewById(R.id.sv_filter_tempat_wisata);
        searchViewTujuanWisata.setQueryHint(getString(R.string.cari_tempat));

        rvTempatWisata = this.view.findViewById(R.id.rv_tempat_wisata);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTempatWisata.setLayoutManager(linearLayoutManager);
        mAdapter = new TempatWisataAdapter(getActivity());
        rvTempatWisata.setAdapter(mAdapter);
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();

        searchViewTujuanWisata.setOnClickListener(this);
        searchViewTujuanWisata.setOnQueryTextListener(this);

        return this.view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sv_filter_tempat_wisata:
                searchViewTujuanWisata.setIconified(false);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mAdapter.filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.filter(s);
        return true;
    }
}

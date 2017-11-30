package com.example.fauza.golang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fauza.golang.R;

public class FragmentHomeTourGuide extends Fragment {

    private View view;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_home_tourguide, container, false);
        this.textView = this.view.findViewById(R.id.tv_list_request);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return this.view;
    }
}

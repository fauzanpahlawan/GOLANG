package com.example.fauza.golang;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class RequestTourGuideActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private TextView textViewJumlahWisatawan;
    private TextView textViewTanggalWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tour_guide);

        TextView textViewNamaTempat = findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = findViewById(R.id.tv_tanggal_wisata);
        Button buttonMinus = findViewById(R.id.bt_minus);
        Button buttonPlus = findViewById(R.id.bt_plus);

        buttonMinus.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        textViewTanggalWisata.setOnClickListener(this);

        setNamaTempat(textViewNamaTempat);

    }

    int lastJumlah = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_minus:
                if (lastJumlah > 0) {
                    String placeholder = --lastJumlah + "";
                    textViewJumlahWisatawan.setText(placeholder);
                }
                break;
            case R.id.bt_plus:
                String placeholder = ++lastJumlah + "";
                textViewJumlahWisatawan.setText(placeholder);
                break;
            case R.id.tv_tanggal_wisata:
                showDatePickerDialog(view);
                break;
            case R.id.bt_buat_request:
                //TODO Write to Firebase
                //TODO Intent to Home Member
                break;
        }
    }

    public void setNamaTempat(TextView textView) {
        Intent intent = getIntent();
        final String NAMA_TEMPAT = "nama_tempat";
        String namaTempat = intent.getStringExtra(NAMA_TEMPAT);
        textView.setText(namaTempat);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = day + "/" + month + "/" + year;
        textViewTanggalWisata.setText(date);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (RequestTourGuideActivity) getActivity(), year, month, day);
        }
    }
}

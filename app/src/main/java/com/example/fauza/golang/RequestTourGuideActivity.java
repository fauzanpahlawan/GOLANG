package com.example.fauza.golang;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class RequestTourGuideActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private final String NAMA_TEMPAT = "nama_tempat";
    private TextView textViewNamaTempat;
    private TextInputEditText textInputEditTextJumlahWisatawan;
    private TextView textViewTanggalWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tour_guide);

        textViewNamaTempat = findViewById(R.id.tv_nama_tempat);
        textInputEditTextJumlahWisatawan = findViewById(R.id.et_jumlah_wisatawan);
        textViewTanggalWisata = findViewById(R.id.tv_tanggal_wisata);

        textViewTanggalWisata.setOnClickListener(this);

        setNamaTempat(textViewNamaTempat);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tanggal_wisata:
                showDatePickerDialog(view);
                break;
        }
    }

    public void setNamaTempat(TextView textView) {
        Intent intent = getIntent();
        String namaTempat = intent.getStringExtra(NAMA_TEMPAT);
        textView.setText(namaTempat);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}

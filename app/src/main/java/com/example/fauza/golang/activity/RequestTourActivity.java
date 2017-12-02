package com.example.fauza.golang.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fauza.golang.R;
import com.example.fauza.golang.model.TourGuideRequest;
import com.example.fauza.golang.utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class RequestTourActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, ValueEventListener {

    private TextView textViewJumlahWisatawan;
    private TextView textViewTanggalWisata;
    private TextView textViewTempatWisata;
    private Toolbar toolbarMain;
    private Button buttonMinus;
    private Button buttonPlus;
    private Button buttonBuatRequest;


    /**
     * Firebase instances
     */
    private FirebaseUtils firebaseUtils = new FirebaseUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tour);

        textViewTempatWisata = findViewById(R.id.tv_nama_tempat);
        textViewJumlahWisatawan = findViewById(R.id.tv_jumlah_wisatawan);
        textViewTanggalWisata = findViewById(R.id.tv_tanggal_wisata);
        buttonMinus = findViewById(R.id.bt_minus);
        buttonPlus = findViewById(R.id.bt_plus);
        buttonBuatRequest = findViewById(R.id.bt_buat_request);
        toolbarMain = findViewById(R.id.toolbar_main);

        buttonMinus.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonBuatRequest.setOnClickListener(this);
        textViewTanggalWisata.setOnClickListener(this);

        setNamaTempat(textViewTempatWisata);
        setSupportActionBar(toolbarMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
                //COMPLETED Write to Firebase
                try {
                    TourGuideRequest tourGuideRequest = new TourGuideRequest(
                            textViewTempatWisata.getText().toString(),
                            textViewTanggalWisata.getText().toString(),
                            textViewJumlahWisatawan.getText().toString(),
                            getString(R.string.BELUM_ADA_TOUR_GUIDE)
                    );
                    firebaseUtils.getRef().child(getString(R.string.TOUR_REQUESTS))
                            .child(firebaseUtils.getUser().getUid())
                            .setValue(tourGuideRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                RequestTourActivity.this.finish();
                            } else {
                                if (task.getException() != null) {
                                    Toast.makeText(RequestTourActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    public static class DatePickerFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (RequestTourActivity) getActivity(), year, month, day);
        }
    }
}

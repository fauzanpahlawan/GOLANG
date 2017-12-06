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
import android.widget.ImageButton;
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

public class RequestTourGuideActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, ValueEventListener {

    private TextView textViewJumlahWisatawan;
    private TextView textViewTanggalWisata;
    private TextView textViewTempatWisata;
    private Toolbar toolbarMain;
    private ImageButton buttonMinus;
    private ImageButton buttonPlus;
    private Button buttonBuatRequest;


    /**
     * Firebase instances
     */
    private FirebaseUtils firebaseUtils = new FirebaseUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tour_guide);

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
                createTourGuideRequest();
//                TourGuideRequest tourGuideRequest = new TourGuideRequest(
//                        firebaseUtils.getUser().getUid(),
//                        getString(R.string.idTourGuide),
//                        RequestTourGuideActivity.this.getResources().getInteger(R.integer.TOUR_STATUS_CREATED),
//                        firebaseUtils.getUser().getUid() + "_" + getString(R.string.TOUR_STATUS_INPROGRESS),
//                        getString(R.string.idTourGuide) + "_" + getString(R.string.TOUR_STATUS_INPROGRESS),
//                        textViewTempatWisata.getText().toString(),
//                        textViewJumlahWisatawan.getText().toString(),
//                        textViewTanggalWisata.getText().toString()
//                );
//                final String keyTourGuideRequest = firebaseUtils.getRef()
//                        .child(getString(R.string.tourGuideRequests))
//                        .push()
//                        .getKey();
//                firebaseUtils.getRef()
//                        .child(getString(R.string.tourGuideRequests))
//                        .child(keyTourGuideRequest)
//                        .setValue(tourGuideRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            RequestTourGuideActivity.this.finish();
//                        } else {
//                            if (task.getException() != null) {
//                                Toast.makeText(RequestTourGuideActivity.this, task.getException().getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
                break;
        }
    }

    public void createTourGuideRequest() {
        TourGuideRequest tourGuideRequest = new TourGuideRequest(
                firebaseUtils.getUser().getUid(),
                getString(R.string.idTourGuide),
                RequestTourGuideActivity.this.getResources().getInteger(R.integer.TOUR_STATUS_CREATED),
                firebaseUtils.getUser().getUid() + "_" + getString(R.string.TOUR_STATUS_INPROGRESS),
                getString(R.string.idTourGuide) + "_" + getString(R.string.TOUR_STATUS_INPROGRESS),
                textViewTempatWisata.getText().toString(),
                textViewJumlahWisatawan.getText().toString(),
                textViewTanggalWisata.getText().toString()
        );
        final String keyTourGuideRequest = firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .push()
                .getKey();
        firebaseUtils.getRef()
                .child(getString(R.string.tourGuideRequests))
                .child(keyTourGuideRequest)
                .setValue(tourGuideRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            RequestTourGuideActivity.this.finish();
                        } else {
                            if (task.getException() != null) {
                                Toast.makeText(RequestTourGuideActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void setNamaTempat(TextView textView) {
        Intent intent = getIntent();
        String namaTempat = intent.getStringExtra(getString(R.string.nama_tempat));
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

            return new DatePickerDialog(getActivity(), (RequestTourGuideActivity) getActivity(), year, month, day);
        }
    }
}

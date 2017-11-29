package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeMemberTempActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;

    protected GeoDataClient mGeoDataClient;

    private PlaceAutoCompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private TextView textViewPlaceResult;


    private static final LatLngBounds BOUNDS_EAST_JAVA = new LatLngBounds(
            new LatLng(-8.808070, 115.949013),
            new LatLng(-6.753266, 111.343689));


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_member_temp);

        toolbarHome = findViewById(R.id.toolbar_home);
        textViewCurrentUser = findViewById(R.id.textView_current_user);

        // Set textView text with the current signed in user
        setUser();

        mGeoDataClient = Places.getGeoDataClient(this, null);

        mAutocompleteView = findViewById(R.id.ac_tv_places);
        textViewPlaceResult = findViewById(R.id.tv_place_result);

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        mAdapter = new PlaceAutoCompleteAdapter(this, mGeoDataClient, BOUNDS_EAST_JAVA, null);
        mAutocompleteView.setAdapter(mAdapter);

        // Set app logo to account
        toolbarHome.setLogo(R.drawable.ic_account);
        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sign_out:
                FirebaseAuth.getInstance().signOut();
                explicitIntent(this, LoginActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // TODO onClick Handler of various things
        }
    }

    private void explicitIntent(Activity activity, Class _class) {
        Intent explicitIntent = new Intent(activity, _class);
        startActivity(explicitIntent);
    }

    private void setUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.textViewCurrentUser.setText(user.getEmail());
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item != null ? item.getPlaceId() : null;

            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                final Place place = places.get(0);

                // Format details of the place for display and show it in a TextView.
                textViewPlaceResult.setText(
                        formatPlaceDetails(getResources(), place.getName(), place.getAddress()));

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res,
                                              CharSequence name,
                                              CharSequence address) {
        return Html.fromHtml(res.getString(R.string.place_format, name, address));

    }
}

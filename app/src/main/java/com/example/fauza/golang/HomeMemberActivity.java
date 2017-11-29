package com.example.fauza.golang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class HomeMemberActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private TextView textViewCurrentUser;
    private Toolbar toolbarHome;
    private SearchView searchViewTujuanWisata;
    private List<String> data;
    private TempatWisataAdapter mAdapter;
    private RecyclerView rvTempatWisata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_member);

        data = Arrays.asList(getResources().getStringArray(R.array.tempat_wisata));
        Collections.sort(data);

        toolbarHome = findViewById(R.id.toolbar_home);
        textViewCurrentUser = findViewById(R.id.textView_current_user);
        searchViewTujuanWisata = findViewById(R.id.sv_filter_tempat_wisata);
        searchViewTujuanWisata.setQueryHint(getString(R.string.cari_tempat));
        searchViewTujuanWisata.setOnClickListener(this);


        rvTempatWisata = findViewById(R.id.rv_tempat_wisata);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TempatWisataAdapter(this);
        rvTempatWisata.setLayoutManager(linearLayoutManager);
        rvTempatWisata.setAdapter(mAdapter);
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();

        // Set textView text with the current signed in user
        setUser();
        // Set app logo to account
        toolbarHome.setLogo(R.drawable.ic_account);
        setSupportActionBar(toolbarHome);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        searchViewTujuanWisata.setOnQueryTextListener(this);

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
            case R.id.sv_filter_tempat_wisata:
                searchViewTujuanWisata.setIconified(false);
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

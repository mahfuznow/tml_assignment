package com.mahfuznow.assignment.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mahfuznow.assignment.R;
import com.mahfuznow.assignment.controller.DatabaseHandler;
import com.mahfuznow.assignment.databinding.ActivityMapsBinding;
import com.mahfuznow.assignment.model.Person;
import com.mahfuznow.assignment.utils.AppConstants;

import java.util.List;
import java.util.Objects;


public class ExistingLocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private DatabaseHandler db;
    Context context = ExistingLocationsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Existing Locations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showMarkers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    void showMarkers() {
        db = new DatabaseHandler(context);
        List<Person> personList = db.getPeople();
        LatLng latLong = new LatLng(24.3997,89.7772); //default zoom location

        if(personList.isEmpty()) {
            Toast.makeText(context,"There is no location to show",Toast.LENGTH_SHORT).show();
        }
        else {
            for(Person p : personList) {
                latLong = new LatLng(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));
                mMap.addMarker(
                        new MarkerOptions().position(latLong).title(p.getAddress()).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker_icon))
                );
            }
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, AppConstants.DEFAULT_ZOOM));
    }
/*
    void testDataLoading() {
        db = new DatabaseHandler(this);
         db.addPerson(new Person("24.3997","89.7772","9.4","Address 1"));
         db.addPerson(new Person("24.3945","88.7772","9.4","Address 2"));


        List<Person> personList = db.getPeople();
        for(Person p : personList){
            String myInfo = "ID: " + p.getId() + " Lat: " + p.getLatitude() +
                    " Long: "+ p.getLongitude()
                    + " Alt: "+p.getAltitude()
                    + " Add: "+p.getAddress()
                    ;
            Log.d("People data", myInfo);
            Toast.makeText(context,myInfo,Toast.LENGTH_SHORT).show();
        }
        Log.d("all data ", "--------------------------");
        Log.d("person numbers =  ", String.valueOf(db.getNumPerson()) );
    }
 */
}
package com.mahfuznow.assignment.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mahfuznow.assignment.R;
import com.mahfuznow.assignment.controller.DatabaseHandler;
import com.mahfuznow.assignment.model.Person;

import java.util.Objects;

public class LocationEntryActivity extends AppCompatActivity {

    Context context = LocationEntryActivity.this;
    ProgressDialog progressDialog;
    FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_ID = 44;

    String latitude, longitude, altitude, address;
    EditText edtLatitude, edtLongitude, edtAltitude, edtAddress;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_entry);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Location Entry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(context);
        edtLatitude = findViewById(R.id.edt_latitude);
        edtLongitude = findViewById(R.id.edt_longitude);
        edtAltitude = findViewById(R.id.edt_altitude);
        edtAddress = findViewById(R.id.edt_address);
        btnSave = findViewById(R.id.btn_save);

        progressDialog.setMessage("Getting your location...");
        progressDialog.show();
        progressDialog.setCancelable(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // method to get the location
        getLocation();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Saving data....");
                progressDialog.show();

                latitude = edtLatitude.getText().toString();
                longitude = edtLongitude.getText().toString();
                altitude = edtAltitude.getText().toString();
                address = edtAddress.getText().toString();

                if(!TextUtils.isEmpty(latitude) &&!TextUtils.isEmpty(longitude) &&!TextUtils.isEmpty(altitude) &&!TextUtils.isEmpty(address)) {
                    DatabaseHandler db;
                    db = new DatabaseHandler(context);
                    db.addPerson(new Person(latitude, longitude, altitude, address));
                    //24.3997, 89.7772
                    //db.addPerson(new Person("24.3945", "88.7772", "9.4", "Address 2"));
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(context,"Location added successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(context,"Please fill up all the required field",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last location from FusedLocationClientobject
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        requestNewLocationData();
                        /*
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            edtLatitude.setText(String.valueOf(location.getLatitude()));
                            edtLongitude.setText(String.valueOf(location.getLongitude()));
                            edtAltitude.setText(String.valueOf(location.getAltitude()));
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        */
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available, request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        // Initializing LocationRequest object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequeston FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            edtLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
            edtLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
            edtAltitude.setText(String.valueOf(mLastLocation.getAltitude()));
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        // If we want background location on Android 10.0 and higher, use: ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLocation();
        }
    }
}
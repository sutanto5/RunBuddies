package com.example.runbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//https://www.youtube.com/watch?v=g1_jp5vV7co
//https://www.youtube.com/watch?v=-dO23oDmAaE


// Steps to use location manager to reveive location updates in your app
// 1 - Add permissions to the manifest file
// 2 - Implement LocationListener in teh class that requires GPS data
// 3 - Implements LocationListener methods
// 4 - Ask for user permission to use GPS services
// 5 - Start updating locations
// 6 - Recieve updates and update (UI, Database, cloud)
// 7 - Stop updating locations if not needed to save battery

public class SaveRun extends AppCompatActivity implements LocationListener {

    protected double latitude, longitude;
    private TextView latLong, address, distanceView;
    private LocationManager locationManager;
    private Location last;
    private long distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_run);

        //instantiate variables
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        latLong = findViewById(R.id.latLong);
        address = findViewById(R.id.address);
        distanceView = findViewById(R.id.distance);


        //first check and ask permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //checks for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    // @Override
    // protected void onResume() {
    //     super.onResume();
    //     if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
    //         // TODO: Consider calling
    //         //    ActivityCompat#requestPermissions
    //         // here to request the missing permissions, and then overriding
    //         //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //         //                                          int[] grantResults)
    //         // to handle the case where the user grants the permission. See the documentation
    //         // for ActivityCompat#requestPermissions for more details.
    //         return;
    //     }
    //     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    // }
//

    //on button click get the location
    public void getLocation(View view) throws InterruptedException {
        //if permission allowed retrieve location
        retrieveLocation();
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation(){

        //min time is min time between requests and minDistance is distance needed till update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,0,
                this);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location!= null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //convert lat long to address
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try{
                List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                latLong.setText("Lat:" + latitude + " \nLong: " + longitude);
                address.setText(addressList.get(0).getAddressLine(0));
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    //gets permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            retrieveLocation();
        }
        else{
            latLong.setText("Permission Denied");
            address.setText("Permission Denied");
        }
    }

    //Automatic
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //convert lat long to address
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        latitude= location.getLatitude();
        longitude = location.getLongitude();

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            latLong.setText("Latitude:" + latitude + " \nLongitude: " + longitude);
            address.setText(addressList.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(last!=null){
            distance+=location.distanceTo(last);
        }

        last = new Location(location);
        distanceView.setText(distance + " meters");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

}

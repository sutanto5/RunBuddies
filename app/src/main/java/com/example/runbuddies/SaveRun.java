package com.example.runbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    public DecimalFormat decimalFormat = new DecimalFormat(".##");

    // Number of seconds displayed
    // on the stopwatch.
    public int seconds = 0;
    public long distance = 0;
    private int pace = 0;

    // Is the stopwatch running?
    private boolean running;

    //for swipe view
    float x1,x2;

    private boolean wasRunning;
    TextView runTime;
    TextView runPace;
    Button start;
    Button stop;
    Button save;
    Button reset;
    TextView distanceTV;
    TextView paceTV;
    TextView timeTV;

    protected double latitude, longitude;
    private TextView distanceView;
    private LocationManager locationManager;
    private Location last;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_run);
        getSupportActionBar().hide();


        //starts clock
        runTimer();
        //instantiate variables
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        distanceView = findViewById(R.id.distance);
        runTime = findViewById(R.id.time_view);
        runPace = findViewById(R.id.pace_View);
        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        save = findViewById(R.id.save);
        reset = findViewById(R.id.reset);
        distanceTV = findViewById(R.id.distanceTV);
        paceTV = findViewById(R.id.paceTV);
        timeTV = findViewById(R.id.TIMETV);

        //check if activity has already been opened
        seconds = GlobalVars.seconds;
        distance = GlobalVars.distance;


        //first check and ask permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //checks for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    // If the activity is paused,
    // stop the stopwatch.
    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // Start the stopwatch running
    // when the Start button is clicked.
    // Below method gets called
    // when the Start button is clicked.
    public void onClickStart(View view)
    {
        running = true;
       // start.setVisibility(View.GONE);
        start.setText("Resume");
        reset.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickStop(View view)
    {
        running = false;
        reset.setVisibility(View.VISIBLE);
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
    }



    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
        pace = 0;
        distance= 0;

        //resetting global vars
        GlobalVars.seconds = 0;
        GlobalVars.distance = 0;
        Intent intent = new Intent(SaveRun.this,HomePageActivity.class);
        startActivity(intent);
    }
    public void addRun(View view) {
            ArrayList<View> views = new ArrayList<View>(
                            Arrays.asList(start,stop,save,reset,distanceTV,paceTV,timeTV,distanceView,runTime,runPace));
            View nameTV = findViewById(R.id.nameTV);
            EditText name = findViewById(R.id.runNameEditText);
            Button upload = findViewById(R.id.upload);
            for(View v:views) {
                    v.setVisibility(View.GONE);
                }
            name.setVisibility(View.VISIBLE);
            nameTV.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
        }

    public void addRunButtonClicked(View view) {
        EditText nameET = findViewById(R.id.runNameEditText);
        String name = nameET.getText().toString();
        String dist = distanceView.getText().toString();
        String time = runTime.getText().toString();
        String pace = runPace.getText().toString();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = (month + 1) + "/" + (day) + "/" + year;
        Run r = new Run(date, dist, pace, time, name);


        LogInActivity.firebaseHelper.addRunData(r);
        onClickReset(view);

    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer()
    {

        // Get the text view.
        final TextView timeView
                = findViewById(
                R.id.time_view);
        final TextView paceView
                = findViewById(
                R.id.pace_View);
        final TextView distView
                = findViewById(
                R.id.distance);
        // Creates a new Handler
        final Handler handler
                = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run()
            {
                //time stuff
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                if(distance == 0){
                    pace = 0;
                }else{
                    pace = (int)(seconds /(distance));
                }
                int paceHours = pace/ 3600;
                int paceMinutes = (pace % 3600) / 60;
                int paceSecs = pace % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time;
                if(hours >0) {
                    time = String
                            .format(Locale.getDefault(),
                                    "%d:%02d:%02d", hours,
                                    minutes, secs);
                } else {
                    time = String
                            .format(Locale.getDefault(),
                                    "%02d:%02d",
                                    minutes, secs);
                }
                String pace;
                if(paceHours >0) {
                    pace = String
                            .format(Locale.getDefault(),
                                    "%d:%02d:%02d", paceHours,
                                    paceMinutes, paceSecs);
                } else {
                    pace = String
                            .format(Locale.getDefault(),
                                    "%02d:%02d",
                                    paceMinutes, paceSecs);
                }
                pace +="/km";

                // Set the text view text.
                timeView.setText(time);
                paceView.setText(pace);


                retrieveLocation();



                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
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

        running = true;
        start.setVisibility(View.GONE);
        start.setText("Resume");
        reset.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
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
            Toast.makeText(getApplicationContext(), "This app requires location permissions to run", Toast.LENGTH_LONG).show();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(last!=null){
            distance+=location.distanceTo(last);
        }

        last = new Location(location);
        distanceView.setText(decimalFormat.format((double)distance/1000) + "km");
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

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                if(x1 < x2) {
                    Toast.makeText(getApplicationContext(), "This run has been paused", Toast.LENGTH_LONG).show();
                    GlobalVars.seconds = seconds;
                    GlobalVars.distance = distance;
                    Intent i = new Intent(SaveRun.this, MapActivity.class);
                    startActivity(i);

                }
        }
        return false;
    }

}

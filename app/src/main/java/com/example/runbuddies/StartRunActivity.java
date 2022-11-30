package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class StartRunActivity extends AppCompatActivity {
    DecimalFormat decimalFormat = new DecimalFormat(".##");
    // Use seconds, running and wasRunning respectively
    // to record the number of seconds passed,
    // whether the stopwatch is running and
    // whether the stopwatch was running
    // before the activity was paused.

    // Number of seconds displayed
    // on the stopwatch.
    private int seconds = 0;
    private double distance =0;
    private int pace = 0;

    float x1, x2;

    // Is the stopwatch running?
    private boolean running;

    private boolean wasRunning;
    TextView runDistance;
    TextView runTime;
    TextView runPace;
    Button start;
    Button stop;
    Button save;
    Button reset;
    TextView distanceTV;
    TextView paceTV;
    TextView timeTV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);
        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }
        runTimer();
        runTime = findViewById(R.id.time_view);
        runPace = findViewById(R.id.pace_View);
        runDistance = findViewById(R.id.milesView);
        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        save = findViewById(R.id.save);
        reset = findViewById(R.id.reset);
        distanceTV = findViewById(R.id.distanceTV);
        paceTV = findViewById(R.id.paceTV);
        timeTV = findViewById(R.id.TIMETV);
    }

    // Save the state of the stopwatch
    // if it's about to be destroyed.
    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
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
        start.setVisibility(View.GONE);
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
        Intent intent = new Intent(StartRunActivity.this,HomePageActivity.class);
        startActivity(intent);
    }
    public void addRun(View view) {
        ArrayList<View> views = new ArrayList<View>(
                Arrays.asList(start,stop,save,reset,distanceTV,paceTV,timeTV,runDistance,runTime,runPace));
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
        String dist = runDistance.getText().toString();
        String time = runTime.getText().toString();
        String pace = runPace.getText().toString();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = (month+1) + "/" + (day) + "/" + year;
        Run r = new Run(date, dist, pace, time,name);


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
                R.id.milesView);
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
                pace = (int)(seconds /distance);
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
                pace +="/mi";

                // Set the text view text.
                timeView.setText(time);
                paceView.setText(pace);
                distView.setText(decimalFormat.format(distance) + "mi");


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

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                if(x1 < x2) {
                    Intent i = new Intent(StartRunActivity.this, MapActivity.class);
                    startActivity(i);
                }
        }
        return false;
    }
}

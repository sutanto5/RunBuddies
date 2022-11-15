package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
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



    // Is the stopwatch running?
    private boolean running;

    private boolean wasRunning;
    TextView runDistance;
    TextView runTime;
    TextView runPace;
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
        View s = findViewById(R.id.save);
        Button go =findViewById(R.id.startButton);
        View r = findViewById(R.id.reset);
        View st = findViewById(R.id.stopButton);
        go.setVisibility(View.GONE);
        go.setText("Resume");
        r.setVisibility(View.INVISIBLE);
        s.setVisibility(View.INVISIBLE);
        st.setVisibility(View.VISIBLE);
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    public void onClickStop(View view)
    {
        running = false;
        View s = findViewById(R.id.save);
        Button go = findViewById(R.id.startButton);
        View st = findViewById(R.id.stopButton);
        View r = findViewById(R.id.reset);
        r.setVisibility(View.VISIBLE);
        st.setVisibility(View.GONE);
        go.setVisibility(View.VISIBLE);
        s.setVisibility(View.VISIBLE);
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

    public void addRunButtonClicked(View view) {
        String dist = runDistance.getText().toString();
        String time = runTime.getText().toString();
        String pace = runPace.getText().toString();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + "/" + (month+1) + "/" + year;
        Run r = new Run(date, dist, pace, time,"");
        LogInActivity.firebaseHelper.addRunData(r);

    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer()
    {

        // Get the text view.
        final TextView timeView
                = (TextView)findViewById(
                R.id.time_view);
        final TextView paceView
                = (TextView)findViewById(
                R.id.pace_View);
        final TextView distView
                = (TextView)findViewById(
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
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                pace = (int)((int)seconds/distance);
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
                    distance+=0.01;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }
}

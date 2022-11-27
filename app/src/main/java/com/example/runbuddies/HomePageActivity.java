package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }
    public void logOutClicked(View view) {
        LogInActivity.firebaseHelper.logOutUser();
        Intent intent = new Intent(HomePageActivity.this, LogInActivity.class);
        startActivity(intent);
    }
    public void startRun(View view) {
        //I changed this for testing Leo you can decide where to put it
        //and how to format the page
        // it works so...
        //distance broke for some reason sadness
        Intent intent = new Intent(HomePageActivity.this,MapActivity.class);
        startActivity(intent);
    }
    public void viewProfile(View view) {
        Intent intent = new Intent(HomePageActivity.this,MyProfile.class);
        startActivity(intent);
    }
    public void viewRuns(View view) {
        Intent intent = new Intent(HomePageActivity.this,PastRunsActivity.class);
        startActivity(intent);
    }
    public void findMatch(View view) {
        Intent intent = new Intent(HomePageActivity.this,MatchMakingActivity.class);
        startActivity(intent);
    }

}
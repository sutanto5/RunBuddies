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
        Intent intent = new Intent(HomePageActivity.this,StartRunActivity.class);
        startActivity(intent);
    }
    public void viewProfile(View view) {
        Intent intent = new Intent(HomePageActivity.this,MyProfile.class);
        startActivity(intent);
    }
}
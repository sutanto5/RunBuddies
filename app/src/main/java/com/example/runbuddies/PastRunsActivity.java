package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PastRunsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_runs);
    }
    public void goToHome(View view) {
        Intent intent = new Intent(PastRunsActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
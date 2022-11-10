package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MyProfile extends AppCompatActivity {

    private TextView level;
    private TextView state;
    private TextView city;
    private TextView bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        level = findViewById(R.id.levelTextView);
        state = findViewById(R.id.stateTextView);
        city = findViewById(R.id.cityTextView);
        bio = findViewById(R.id.bioTextView);

       
    }


}
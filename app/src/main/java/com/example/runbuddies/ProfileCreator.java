package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ProfileCreator extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    EditText cityET;
    EditText stateET;
    EditText bioET;
    Button 



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);
    }
}
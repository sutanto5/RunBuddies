package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileCreator extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    EditText cityET;
    EditText stateET;
    EditText bioET;
    String spinnerSelectedText;

    public static ArrayList<String> allUsers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        cityET = findViewById(R.id.cityEditText);
        stateET = findViewById(R.id.stateEditText);
        bioET = findViewById(R.id.bioEditText);

        // this attaches my spinner design (spinner_list.xml) and my array of spinner choices(R.array.memoryRating)
        spinner = findViewById(R.id.levelSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_list,
                getResources().getStringArray(R.array.runnerLevels));

        // this attaches my custom row design (how I want each row to look)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelectedText = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), spinnerSelectedText, Toast.LENGTH_SHORT).show();
    }
    // This method is required, even if empty, for the OnItemSelectedListener to work
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    public void addProfileButtonClicked(View view) {
        String city = cityET.getText().toString();
        String state = stateET.getText().toString();
        String runBio = bioET.getText().toString();
        String runLevel = spinnerSelectedText;
        String name = SignUpActivity.getName();

        Profile p = new Profile(city, state, runBio, runLevel);
        Log.d("Denna", p.toString());
        LogInActivity.firebaseHelper.addProfile(p);

        cityET.setText("");
        stateET.setText("");
        bioET.setText("");

        allUsers.add(LogInActivity.firebaseHelper.getMAuth().getUid());

        Intent intent = new Intent(ProfileCreator.this, HomePageActivity.class);
        startActivity(intent);


    }
}
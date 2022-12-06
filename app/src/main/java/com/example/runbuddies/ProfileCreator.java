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

    Spinner spinnerLevel;
    Spinner spinnerState;
    EditText cityET;
    EditText bioET;
    String spinnerSelectedText;
    String otherSpinnerSelectedText;

    public static ArrayList<String> allUsers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);


        class StateSpinnerClass implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedText = parent.getItemAtPosition(position).toString();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }

        class LevelSpinnerClass implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherSpinnerSelectedText = parent.getItemAtPosition(position).toString();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        cityET = findViewById(R.id.cityEditText);
        bioET = findViewById(R.id.bioEditText);

        // this attaches my spinner design (spinner_list.xml) and my array of spinner choices(R.array.memoryRating)
        spinnerLevel = findViewById(R.id.levelSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_list,
                getResources().getStringArray(R.array.runnerLevels));
        spinnerState = findViewById(R.id.stateSpinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_list,
                getResources().getStringArray(R.array.states));

        // this attaches my custom row design (how I want each row to look)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerLevel.setAdapter(adapter);
        spinnerLevel.setOnItemSelectedListener(new LevelSpinnerClass());
        spinnerState.setAdapter(adapter1);
        spinnerState.setOnItemSelectedListener(new StateSpinnerClass());


    }

    public void addProfileButtonClicked(View view) {
        String city = cityET.getText().toString();
        String runBio = bioET.getText().toString();
        String state = spinnerSelectedText;
        String runLevel = otherSpinnerSelectedText;
        String name = SignUpActivity.getName();
        String docID = LogInActivity.firebaseHelper.getMAuth().getUid();
        String email = SignUpActivity.getEmail();

        Profile p = new Profile(city, state, runBio, runLevel, name, docID, email);
        Log.d("Denna", p.toString());
        LogInActivity.firebaseHelper.addProfile(p);

        cityET.setText("");
        bioET.setText("");

        allUsers.add(LogInActivity.firebaseHelper.getMAuth().getUid());

        Intent intent = new Intent(ProfileCreator.this, HomePageActivity.class);
        startActivity(intent);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
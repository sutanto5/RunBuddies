package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {


    public final String TAG = "MOY";
    EditText nameET, bioET, stateET, cityET;
    Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameET = findViewById(R.id.nameEditText);
        bioET = findViewById(R.id.bioEditText);
        stateET = findViewById(R.id.stateEditText);
        cityET = findViewById(R.id.cityEditText);


        currentProfile = LogInActivity.firebaseHelper.getProfile();


        nameET.setText(currentProfile.getName());
        bioET.setText(currentProfile.getBio());
        stateET.setText(currentProfile.getState());
        cityET.setText(currentProfile.getCity());

    }



    public void saveProfileEdits(View v) {
        Log.d(TAG, "inside saveMemoryEdits method");
        // updates the currentMemory object to have the same name/desc that are on the screen
        // in the event of changes made.
        currentProfile.setName(nameET.getText().toString());
        currentProfile.setBio(bioET.getText().toString());
        currentProfile.setState(stateET.getText().toString());
        currentProfile.setCity(cityET.getText().toString());

        // Calls editProfile with this updated Memory object
        LogInActivity.firebaseHelper.editProfile(currentProfile);

    }

    public void goToHome(View view) {
        Log.d(TAG, "Go to home");
        Intent intent = new Intent(EditProfileActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
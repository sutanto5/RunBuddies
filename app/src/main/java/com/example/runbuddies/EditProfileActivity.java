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
    Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameET = findViewById(R.id.nameEditText);
        bioET = findViewById(R.id.bioEditText);
        stateET = findViewById(R.id.stateEditText);
        cityET = findViewById(R.id.cityEditText);


        userProfile = LogInActivity.firebaseHelper.getProfile();


        nameET = findViewById(R.id.nameEditText);
        bioET = findViewById(R.id.bioEditText);
        stateET = findViewById(R.id.stateEditText);
        cityET = findViewById(R.id.cityEditText);

    }



    public void saveMemoryEdits(View v) {
        Log.d(TAG, "inside saveMemoryEdits method");
        // updates the currentMemory object to have the same name/desc that are on the screen
        // in the event of changes made.
        userProfile.setName(nameET.getText().toString());
        userProfile.setBio(bioET.getText().toString());

        // Calls editData with this updated Memory object


        LogInActivity.firebaseHelper.editProfile(userProfile);

    }




    public void goToHome(View view) {
        Intent intent = new Intent(EditProfileActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
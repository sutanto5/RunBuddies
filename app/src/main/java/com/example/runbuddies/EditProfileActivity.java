package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {


    public final String TAG = "Denna";
    EditText nameET, bioET, stateET, cityET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        nameET = findViewById(R.id.nameEditText);
        bioET = findViewById(R.id.bioEditText);
        stateET = findViewById(R.id.stateEditText);
        cityET = findViewById(R.id.cityEditText);
    }




    public void goToHome(View view) {
        Intent intent = new Intent(EditProfileActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
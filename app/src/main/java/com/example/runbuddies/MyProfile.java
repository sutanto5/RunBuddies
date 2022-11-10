package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MyProfile extends AppCompatActivity {

    private TextView level;
    private TextView state;
    private TextView city;
    private TextView bio;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        level = findViewById(R.id.levelTextView);
        state = findViewById(R.id.stateTextView);
        city = findViewById(R.id.cityTextView);
        bio = findViewById(R.id.bioTextView);
        name = findViewById(R.id.nameTextView);


       ArrayList<Profile> myProfiles = LogInActivity.firebaseHelper.getWishListItemProfile();

       level.setText(myProfiles.get(0).getLevel());
       state.setText(myProfiles.get(0).getState());
       city.setText(myProfiles.get(0).getCity());
       bio.setText(myProfiles.get(0).getBio());
       name.setText(myProfiles.get(0).getName());

    }


}
package com.example.runbuddies;

import static com.example.runbuddies.LogInActivity.firebaseHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyProfile extends AppCompatActivity {

    private TextView level;
    private TextView state;
    private TextView city;
    private TextView bio;
    private TextView name;

    public final String TAG = "LIAM";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        level = findViewById(R.id.levelTextView);
        state = findViewById(R.id.stateTextView);
        city = findViewById(R.id.cityTextView);
        bio = findViewById(R.id.bioTextView);
        name = findViewById(R.id.nameTextView);

        ArrayList<Profile> myList = LogInActivity.firebaseHelper.getWishListItemProfile();

        level.setText(myList.get(0).getLevel());
        state.setText(myList.get(0).getState());
        city.setText(myList.get(0).getCity());
        bio.setText(myList.get(0).getBio());
        name.setText(myList.get(0).getName());


        }
}



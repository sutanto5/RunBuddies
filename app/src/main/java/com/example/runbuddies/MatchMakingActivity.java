package com.example.runbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.DatabaseInfo;

import java.util.ArrayList;
import java.util.UUID;

public class MatchMakingActivity extends AppCompatActivity {

   final String TAG = "Liam";

   private ListView myMatchesListView;

    private FirebaseHelper.FirestoreCallback firestoreCallback;

   //ArrayList<String> users = new ArrayList<>();
    private ArrayList<Profile> users = new ArrayList<>();
    private ArrayList<Profile> matches = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        myMatchesListView = findViewById(R.id.MatchesListView);

       users = LogInActivity.firebaseHelper.getAllProfiles(firestoreCallback);

       for(Profile P: users){
           if(P.getLevel().equals(Profile.getLevel()) && P.getState().equals(Profile.getState())){
               matches.add(P);
           }
       }

       ArrayAdapter<Profile> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

       myMatchesListView.setAdapter(listAdapter);


       }




}
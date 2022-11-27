package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.DatabaseInfo;

import java.util.ArrayList;
import java.util.UUID;

public class MatchMakingActivity extends AppCompatActivity {

   final String TAG = "Liam";

   public DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
   public DatabaseReference usersdRef = rootRef.child("users");

   //ArrayList<String> users = new ArrayList<>();
    private ArrayList<String> users;

   TextView all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        users = new ArrayList<>();


       all = findViewById(R.id.textView2);

       ValueEventListener eventListener = new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot ds : dataSnapshot.getChildren()) {

                   String name = ds.child("users").getValue(String.class);

                   Log.d(TAG, name);

                   users.add(name);

               }

               // ArrayAdapter<String> adapter = new ArrayAdapter(MatchMakingActivity.this, android.R.layout.simple_list_item_1, users);

               // mListView.setAdapter(adapter);



           }

           @Override

           public void onCancelled(DatabaseError databaseError) {

           }
       };
       usersdRef.addListenerForSingleValueEvent(eventListener);
       all.setText("Hi" + users.toString());

    }


}
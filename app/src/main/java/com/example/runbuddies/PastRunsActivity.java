package com.example.runbuddies;

import static com.example.runbuddies.LogInActivity.firebaseHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PastRunsActivity extends AppCompatActivity {
    private ListView myRunsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_runs);
        // find listView in xml
        myRunsListView = findViewById(R.id.allRunsListView);
        // get ArrayList of data from firebase
        ArrayList<Run> myList = LogInActivity.firebaseHelper.getRunArrayList();
        // bind data to the ArrayAdapter (this is a default adapter
        // The text shown is based on the Memory class toString
        ArrayAdapter<Run> listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, myList);
        // attaches the listAdapter to my listView
        myRunsListView.setAdapter(listAdapter);
        // if did custom array set up, use this one
    }

    public void goToHome(View view) {
        Intent intent = new Intent(PastRunsActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
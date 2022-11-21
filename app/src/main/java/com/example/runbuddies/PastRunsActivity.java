package com.example.runbuddies;

import static com.example.runbuddies.LogInActivity.firebaseHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PastRunsActivity extends AppCompatActivity {
    private ListView myRunsListView;
    public static final String CHOSEN_RUN = "chosen run";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_runs);
        // find listView in xml
        myRunsListView = findViewById(R.id.allRunsListView);
        // get ArrayList of data from firebase
        ArrayList<Run> myList = LogInActivity.firebaseHelper.getWishListItemsRuns();
        // bind data to the ArrayAdapter (this is a default adapter
        // The text shown is based on the Memory class toString
        ArrayAdapter<Run> listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, myList);
        // attaches the listAdapter to my listView
        myRunsListView.setAdapter(listAdapter);
        // if did custom array set up, use this one
        // Create listener to listen for when a Run from the specific Category list is clicked on
        myRunsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Creates an intent to go from the Specific Category to the specific Detail
                Intent intent = new Intent(PastRunsActivity.this, EditRunActivity.class);
                // Sends the specific object at index i to the Detail activity
                // In this case, it is sending the particular Food object
                intent.putExtra(CHOSEN_RUN, myList.get(position));
                startActivity(intent);
            }
        });
    }


    public void goToHome(View view) {
        Intent intent = new Intent(PastRunsActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
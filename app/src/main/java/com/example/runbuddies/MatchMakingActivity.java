package com.example.runbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.DatabaseInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class MatchMakingActivity extends AppCompatActivity {

    final String TAG = "Liam";

    private ListView myMatchesListView;

    //ArrayList<String> users = new ArrayList<>();
    private ArrayList<Profile> users = new ArrayList<>();
    private ArrayList<Profile> myMatches;

    public static final String DETAIL_CHOICE = "Chosen Profile Match";

    // private FirebaseHelper.FirestoreCallback firestoreCallback;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        myMatches  = new ArrayList<Profile>();




    /*
    This firebase code shoudl be in firebasehelper and you would call a method here to
    access this this.
    first get the data for logged in users level, state with current user uid
    instantiate myMatches arraylist

        get all users and if level.equals() and state.equals the data from user profile logged in
        AND uid of user != your uid
        add this profile to arraylist
        then load into listview


     */

        myMatches = LogInActivity.firebaseHelper.getMatches();
        Intent intent = getIntent();
        Log.d(TAG, "myMatches.size = " + myMatches.size());
        //myMatches = intent.getParcelableArrayListExtra(LogInActivity.ARRAYLIST_VALUES);



        ArrayAdapter<Profile> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);

        ProfileAdapter myProfileAdapter = new ProfileAdapter(this, myMatches);

        // This finds the listView and then adds the adapter to bind the data to this view
        ListView listView = findViewById(R.id.MatchesListView);
        listView.setAdapter(listAdapter);


        listView.setAdapter(myProfileAdapter);

        // Create listener to listen for when a Food from the specific Category list is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Creates an intent to go from the Specific Category to the specific Detail
                Intent intent = new Intent(MatchMakingActivity.this, MatchProfileActivity.class);
                // Sends the specific object at index i to the Detail activity
                // In this case, it is sending the particular Food object
                Log.d("LIAM",  myMatches.get(position).getName());
                intent.putExtra(DETAIL_CHOICE, myMatches.get(position));

                startActivity(intent);
            }
        });

    }
}





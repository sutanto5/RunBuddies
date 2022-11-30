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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.DatabaseInfo;

import java.util.ArrayList;
import java.util.UUID;

public class MatchMakingActivity extends AppCompatActivity {

    final String TAG = "Liam";

    private ListView myMatchesListView;

    //ArrayList<String> users = new ArrayList<>();
    private ArrayList<Profile> users = new ArrayList<>();
    private ArrayList<Profile> matches = new ArrayList<>();
   // private FirebaseHelper.FirestoreCallback firestoreCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        myMatchesListView = findViewById(R.id.MatchesListView);

       /* users = LogInActivity.firebaseHelper.getAllProfiles(firestoreCallback);


        ArrayList<Profile> myList = LogInActivity.firebaseHelper.getWishListItemProfile();
        for (Profile P : users) {
            if (P.getLevel().equals(myList.get(0).getLevel()) && P.getState().equals(myList.get(0).getState())) {
                matches.add(P);
            }
        }

        ArrayAdapter<Profile> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        myMatchesListView.setAdapter(listAdapter);

*/

       /* FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String level = document.getString("level");
                        String state = document.getString("state");
                        String uid = document.getId();
                        DocumentReference uidRef = db.collection("users").document(uid).collection("myProfile");
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String icPassport = document.getString("ic_passport");
                                        String phone = document.getString("phone");
                                        String ticketDate = document.getString("ticket_date");

                                        Log.d(TAG, level + "/" + state + "/" + icPassport + "/" + phone + "/" + ticketDate);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });*/
    }
}





package com.example.runbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MatchProfileActivity extends AppCompatActivity {

    final String TAG = "LIAM";
    private TextView matchName;
    private TextView matchEmail;
    private TextView matchState;
    private TextView matchCity;
    private TextView matchBio;
    private ImageView ivMatchPicture;
    public static Profile p;


    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        p = MatchMakingActivity.chosen;
        Log.d(TAG,p.getName());

        matchName = findViewById(R.id.matchNameTextView);
        matchEmail = findViewById(R.id.matchEmailTextView);
        matchState = findViewById(R.id.matchStateTextView);
        matchCity = findViewById(R.id.matchCityTextView);
        matchBio = findViewById(R.id.matchBioTextView);
        ivMatchPicture = findViewById(R.id.matchPicture);

        matchName.setText(p.getName());
        matchEmail.setText(p.getEmail());
        matchState.setText(p.getState());
        matchCity.setText(p.getCity());
        matchBio.setText(p.getBio());

        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("images/"+ LogInActivity.firebaseHelper.getMAuth().getUid());
        //file size increase to 5 mb
        final long ONE_MEGABYTE = 1024 * 1024 *5;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //on success set the image to the image view through use of bitmpa
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ivMatchPicture.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void backToHome(View view){
        Intent intent = new Intent(MatchProfileActivity.this, HomePageActivity.class);
        startActivity(intent);
    }

    public void backToMatches(View view){
        Intent intent = new Intent(MatchProfileActivity.this, MatchMakingActivity.class);
        startActivity(intent);
    }
}
package com.example.runbuddies;

import static com.example.runbuddies.LogInActivity.firebaseHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyProfile extends AppCompatActivity {

    private  TextView level;
    private  TextView state;
    private  TextView city;
    private  TextView bio;
    private  TextView name;
    private  ImageView profile;

    public final String TAG = "LIAM";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        level = findViewById(R.id.levelTextView);
        state = findViewById(R.id.stateTextView);
        city = findViewById(R.id.cityTextView);
        bio = findViewById(R.id.bioTextView);
        name = findViewById(R.id.nameTextView);
        profile = findViewById(R.id.picture);

        ArrayList<Profile> myList = LogInActivity.firebaseHelper.getWishListItemProfile();

        level.setText(myList.get(0).getLevel());
        state.setText(myList.get(0).getState());
        city.setText(myList.get(0).getCity());
        bio.setText(myList.get(0).getBio());
        name.setText(myList.get(0).getName());
        // Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("images/"+ LogInActivity.firebaseHelper.getMAuth().getUid());
        //file size increase to 5 mb
        final long ONE_MEGABYTE = 1024 * 1024 *5;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //on success set the image to the image view through use of bitmpa
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

        }



    public void backToHome(View view){
        Intent intent = new Intent(MyProfile.this, HomePageActivity.class);
        startActivity(intent);
    }


}



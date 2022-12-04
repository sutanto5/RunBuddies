package com.example.runbuddies;

import static com.example.runbuddies.LogInActivity.firebaseHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditRunActivity extends AppCompatActivity {

    public final String TAG = "Denna";
    EditText nameET, descET;
    TextView paceTV, timeTV, distTV, dateTV;
    ImageView mileageMessage;
    Run currentRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_run);

        nameET = findViewById(R.id.runNameTV);
        descET = findViewById(R.id.runDescTV);
        distTV = findViewById(R.id.distTv);
        timeTV = findViewById(R.id.timeTV);
        paceTV = findViewById(R.id.paceView);
        dateTV = findViewById(R.id.dateView);
        mileageMessage = findViewById(R.id.RunImage);


        // gets intent from ViewAllMemoriesActivity and retrieves the selected Memory
        // the viewer wanted to see.
        Intent intent = getIntent();
        currentRun = intent.getParcelableExtra(PastRunsActivity.CHOSEN_RUN);
        // Sets the name and desc from the chosen memory
        // Right now I don't have any options to edit the rating.
        nameET.setText(currentRun.getName());
        descET.setText(currentRun.getDesc());
        distTV.setText(currentRun.getDistance());
        timeTV.setText(currentRun.getTime());
        paceTV.setText(currentRun.getPace());
        dateTV.setText(currentRun.getDate());
        mileageMessage.setImageDrawable(getFunnyImage());
    }


    public void saveRunEdits(View v) {
        Log.d(TAG, "inside saveMemoryEdits method");
        // updates the currentMemory object to have the same name/desc that are on the screen
        // in the event of changes made.
        currentRun.setName(nameET.getText().toString());
        currentRun.setDesc(descET.getText().toString());

        // Calls editData with this updated Memory object
        firebaseHelper.editData(currentRun);
        goBack(v);
    }

    public void deleteRun(View v) {
        Log.d(TAG, "deleting run " + currentRun.getName());
        firebaseHelper.deleteData(currentRun);
    }

    public void goBack(View v) {
        Log.d(TAG, "go back");
        Intent intent = new Intent(EditRunActivity.this, PastRunsActivity.class);
        startActivity(intent);
    }

    public Drawable getFunnyImage() {
        ArrayList<Drawable> runMemes = new ArrayList<>();
        runMemes.add(getDrawable(R.drawable.bear));
        runMemes.add(getDrawable(R.drawable.crybaby_meme));
        runMemes.add(getDrawable(R.drawable.crying_george2));
        runMemes.add(getDrawable(R.drawable.everyday_run));
        runMemes.add(getDrawable(R.drawable.slowrun));
        runMemes.add(getDrawable(R.drawable.imagine));
        runMemes.add(getDrawable(R.drawable.insanity));
        runMemes.add(getDrawable(R.drawable.muscles));
        runMemes.add(getDrawable(R.drawable.thatdbegreat));

        int rand = (int)(Math.random() * runMemes.size());

        return runMemes.get(rand);
    }
}


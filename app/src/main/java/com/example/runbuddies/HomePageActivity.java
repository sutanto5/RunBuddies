package com.example.runbuddies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }
    public void logOutClicked(View view) {
        LogInActivity.firebaseHelper.logOutUser();
        Intent intent = new Intent(HomePageActivity.this, LogInActivity.class);
        startActivity(intent);
    }
    public void startRun(View view) {
        //I changed this for testing Leo you can decide where to put it
        //and how to format the page
        // it works so...
        //distance broke for some reason sadness
        Intent intent = new Intent(HomePageActivity.this,MapActivity.class);
        startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.profileButton:
                Intent intent2 = new Intent(HomePageActivity.this, MyProfile.class);
                this.startActivity(intent2);
                return true;
            case R.id.settingsButton:
                Intent intent3 = new Intent(HomePageActivity.this, SettingsActivity.class);
                this.startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewRuns(View view) {
        Intent intent = new Intent(HomePageActivity.this,PastRunsActivity.class);
        startActivity(intent);
    }

    public void findMatch(View view) {
        Intent intent = new Intent(HomePageActivity.this,MatchMakingActivity.class);
        startActivity(intent);
    }

}
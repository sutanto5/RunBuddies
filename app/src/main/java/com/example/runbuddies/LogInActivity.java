package com.example.runbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LogInActivity extends AppCompatActivity {

    Button logInButton;
    Button signUpButton;
    EditText emailET;
    EditText passwordET;

    String email;
    String password;

    public static FirebaseHelper firebaseHelper;

    public final String TAG = "Buddies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseHelper = new FirebaseHelper();
        logInButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
    }

    public void onStart(){
        super.onStart();

        updateUI();
    }

    /**
     * This method will check to see if a user is already signed it to the app.
     * If a user is signed in, then they will be taken to SelectActionActivity
     * instead of loading this main screen
     */
    public void updateUI(){
        Log.d(TAG, "Inside updateUI: " + firebaseHelper.getMAuth().getUid());
        if(firebaseHelper.getMAuth().getUid() != null){
            firebaseHelper.attachReadDataToUser();
            Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Method first checks if the input is valid.  If it meets the screening criteria from
     * getValues(), then the username (which is an email) and password are sent to the FirebaseHelper
     * class to call upon a method of Firebase auth called createUserWithEmailAndPassword
     *
     * This method will return a result once it is complete, and we are listening for that result
     *
     * No matter what, the method will complete, either successfully or not
     * If successfully, that means the user account was created and we can now access the UID of
     * the signed in user.
     * After doing that we switch screens to the SelectActionActivity
     *
     * If unsuccessful, then we will Log that we failed, and include the exception message which will help
     * us gain insight into WHY it didn't work.
     *
     * @param view
     */


    public void signUpClicked(View view) {
        Log.i(TAG, "Sign up clicked");
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void logInClicked(View view) {
        Log.i(TAG, "Log in clicked");
        if (getValues()) {        // get username and password
            // if valid, log in user and then switch to next activity
            // Try to sign into an account using auth with given email and password
            firebaseHelper.getMAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                // Sign in success, update currently signed in user's info
                                firebaseHelper.updateUId(firebaseHelper.getMAuth().getUid());

                                // we will implement this later
                                // updateIfLoggedIn();
                                // firebaseHelper.attachReadDataToUser();

                                Log.d(TAG, email + " logged in");

                                Intent intent = new Intent(LogInActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }
                            else {
                                 /*
                                 This notifies the user of WHY they couldn't log in

                                 https://stackoverflow.com/questions/37859582/how-to-catch-a-firebase-auth-specific-exceptions

                                */

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    // wrong password
                                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Log in failed for " + email + " " + password + e.getMessage());
                                } catch (FirebaseAuthInvalidUserException e) {
                                    // wrong email, no user found with this email
                                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Log in failed for " + email + " " + password + e.getMessage());
                                } catch (Exception e) {
                                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Log in failed for " + email + " " + password + e.getMessage());
                                }
                            }
                            // this log message will tell the name of the exception.  If you want to add this to the catch
                            // statement above, then just add another catch above the generic one at the end

                            Log.d(TAG, "Log in failed for " + email + " " + password +
                                    " because of \n"+ task.getException());


                        }
                    });




            // if invalid - prompt message that says why signin won't go through
        }
    }

    public boolean getValues() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();

        // verify all user data is entered
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // verify password is at least 6 char long (otherwise firebase will deny)
        else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 char long", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Log.i(TAG, email + " " + password + " is set after getValues(), return true");
            email = removeTrailingSpaces(email);
            return true;
        }
    }

    /**
     * This method accepts the email the user wants to submit for FirebaseAuth
     * and removes an extra spaces that may have accidentally been added at the end by
     * the auto-correct keyboard.  This typically happens when the email is used all
     * the time and shows up as a suggestion for the user.
     *
     * @param email
     * @return a String without trailing spaces
     */
    private String removeTrailingSpaces(String email) {
        String lastChar = email.substring(email.length() -1);
        if (lastChar.equals(" "))
            email = email.substring(0, email.length()-1);
        return email;
    }

}
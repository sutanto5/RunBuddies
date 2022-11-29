package com.example.runbuddies;

import static com.example.runbuddies.LogInActivity.firebaseHelper;

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


public class SignUpActivity extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    EditText nameET;
    Button signUpButton;

    final String TAG = "Buddies";

    public static String email;
    String password;
    public static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailET = findViewById(R.id.editTextEmailAddress);
        passwordET = findViewById(R.id.editTextPassword);
        nameET = findViewById(R.id.editTextName);

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
        if (getValues()) {      // get username and password
            // Try to create an account using auth
            firebaseHelper.getMAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // FOR SOME REASON IF THE CREATE USER IS NOT WORKING, THIS IS CRASHING
                            // NOT SURE WHY?  I TRIED WITH A DUPLICATE EMAIL ADDRESS AND THAT CRASHED IT.
                            // LOGCAT SHOWED THE MESSAGE BUT I COULDN'T GET IT TO SHOW IN A LOG STATEMENT

                            if (task.isSuccessful()){
                                // Sign up successful, update UI with the currently signed in user's info
                                firebaseHelper.updateUId(firebaseHelper.getMAuth().getUid());
                                Log.d(TAG, email + " created and logged in");

                                // we will implement this later
                                // updateIfLoggedIn();
                                // firebaseHelper.attachReadDataToUser();

                                Intent intent = new Intent(SignUpActivity.this, ProfilePicture.class);
                                startActivity(intent);
                            }
                            else {
                                /*
                                This prevents the app from CRASHING when the user enters bad items
                                (duplicate email or badly formatted email most likely)

                                 https://stackoverflow.com/questions/37859582/how-to-catch-a-firebase-auth-specific-exceptions

                                 */

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    // poorly formatted email address
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Sign up failed for " + email + " " + password + e.getMessage());
                                } catch (FirebaseAuthEmailException e) {
                                    // duplicate email used
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Sign up failed for " + email + " " + password + e.getMessage());
                                } catch (Exception e) {
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Sign up failed for " + email + " " + password + e.getMessage());
                                }


                                // this log message will tell the name of the exception.  If you want to add this to the catch
                                // statement above, then just add another catch above the generic one at the end

                                Log.d(TAG, "Sign up failed for " + email + " " + password +
                                        " because of \n"+ task.getException());

                            }
                        }
                    });
        }
        else {
            Log.d(TAG, "Failed to pass getValues() method");
        }
    }

    public boolean getValues() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        name = nameET.getText().toString();

        // verify all user data is entered
        if (email.length() == 0 || password.length() == 0 || name.length() == 0) {
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

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        SignUpActivity.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        SignUpActivity.name = name;
    }
}
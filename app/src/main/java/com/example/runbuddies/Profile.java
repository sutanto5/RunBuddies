package com.example.runbuddies;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private String city;
    private String state;
    private String bio;
    private String docID;
    private String name;

    public Profile(String city, String state, String bio, String docID) {
        this.city = city;
        this.state = state;
        this.bio = bio;
        this.docID = docID;
        name = SignUpActivity.getName();
    }

    public Profile(String city, String state, String bio){
        this.city = city;
        this.state = state;
        this.bio = bio;
        docID = "no DocID yet";
        name = SignUpActivity.getName();
    }

    public Profile(){
        city = "no city";
        state = "no state";
        bio = "no bio";
        docID = "no DocID yet";
        name = "no Name";
    }

    /**
     * This is a "constructor" of sorts that is needed with the Parceable interface to
     * tell the intent how to create a Memory object when it is received from the intent
     * basically it is setting each instance variable as a String or Int
     * <p>
     * MAKE SURE THE ORDER OF THESE VARS IS CONSISTENT WITH ALL CONSTRUCTOR TYPE METHODS
     *
     * @param parcel the parcel that is received from the intent
     */
    public Profile(Parcel parcel){
        city = parcel.readString();
        state = parcel.readString();
        bio = parcel.readString();
        docID = parcel.readString();
        name = SignUpActivity.getName();
    }
}
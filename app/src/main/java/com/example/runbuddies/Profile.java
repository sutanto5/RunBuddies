package com.example.runbuddies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Profile implements Parcelable {
    private  String city;
    private  String state;
    private  String bio;
    private  String level;
    private  String docID;
    private  String name;

    public Profile(String city, String state, String bio, String level, String name, String docID) {
        this.city = city;
        this.state = state;
        this.bio = bio;
        this.level = level;
        this.docID = docID;
        this.name = name;
    }


    public Profile(String city, String state, String bio, String level, String docID){
        this.city = city;
        this.state = state;
        this.bio = bio;
        this.level = level;
        this.docID = docID;
        name = SignUpActivity.getName();
    }

    public Profile() {
        city = "no city";
        state = "no state";
        bio = "no bio";
        level = "no level";
        docID = "no DocID yet";
        name = "no Name";
    }

    public String toString() {
        return "Profile{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", bio='" + bio + '\'' +
                ", level='" + level + '\'' +
                ", docID='" + docID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean matches(Profile other){
        return this.level.equals(other.level) && this.state.equals(other.state) && !this.docID.equals(other.docID);
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
        level = parcel.readString();
        docID = parcel.readString();
        name = parcel.readString();
    }

    /**
     * This is what is used when we send the Memory object through an intent
     * It is also a method that is part of the Parceable interface and is needed
     * to set up the object that is being sent.  Then, when it is received, the
     * other Memory constructor that accepts a Parcel reference can "unpack it"
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(bio);
        dest.writeString(level);
        dest.writeString(docID);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {

        @Override
        public Profile createFromParcel(Parcel parcel) {
            return new Profile(parcel);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[0];
        }
    };

    /**
     * This method is required for the Parceable interface.  As of now, this method
     * is in the default state and doesn't really do anything.
     *
     * If your Parcelable class will have child classes, you'll need to
     * take some extra care with the describeContents() method. This would
     * let you identify the specific child class that should be created by
     * the Parcelable.Creator. You can read more about how this works on
     *  Stack Overflow with this link.
     *           https://stackoverflow.com/questions/4778834/purpose-of-describecontents-of-parcelable-interface
     * @return
     */

    @Override
    public int describeContents() {
        return 0;
    }

    public  String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public  String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public  String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public  String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }

    public  String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
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
    private  String email;


    public Profile(String city, String state, String bio, String level, String name, String docID, String email) {
        this.city = city;
        this.state = state;
        this.bio = bio;
        this.level = level;
        this.docID = docID;
        this.name = name;
        this.email = email;
    }


    public Profile(String city, String state, String bio, String level, String docID){
        this.city = city;
        this.state = state;
        this.bio = bio;
        this.level = level;
        this.docID = docID;
        name = SignUpActivity.getName();
        email = SignUpActivity.getEmail();
    }

    public Profile() {
        city = "no city";
        state = "no state";
        bio = "no bio";
        level = "no level";
        docID = "no DocID yet";
        name = "no Name";
        email = "no email";
    }

    public String toString() {
        return "Profile{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", bio='" + bio + '\'' +
                ", level='" + level + '\'' +
                ", docID='" + docID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.bio);
        dest.writeString(this.level);
        dest.writeString(this.name);
        dest.writeString(this.docID);
        dest.writeString(this.email);
    }



    protected Profile(Parcel in) {
        this.city = in.readString();
        this.state = in.readString();
        this.bio = in.readString();
        this.level = in.readString();
        this.name = in.readString();
        this.docID = in.readString();

        this.email = in.readString();
    }


}
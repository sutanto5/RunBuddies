package com.example.runbuddies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;


public class Run implements Parcelable {

    private String date;
    private String distance;
    private String pace;
    private String time;
    private String name;
    private String docID;

    public Run(String date, String distance, String pace, String time, String name, String docID) {
        this.date = date;
        this.distance = distance;
        this.pace = pace;
        this.time = time;
        this.name = name;
        this.docID = docID;
    }

    public Run(String date, String distance, String pace, String time, String name) {
        this.date = date;
        this.distance = distance;
        this.pace = pace;
        this.time = time;
        this.name = name;
        this.docID = "No docID yet";
    }

    public Run() {
        date = "no date";
        distance = "0 mi";
        pace = "0 /mile";
        time = "0 min";
        name = "no name";
        docID = "No docID yet";
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
    public Run(Parcel parcel) {
        date = parcel.readString();
        distance = parcel.readString();
        pace = parcel.readString();
        time = parcel.readString();
        name = parcel.readString();
        docID = parcel.readString();
    }

    /**
     * This is what is used when we send the Memory object through an intent
     * It is also a method that is part of the Parceable interface and is needed
     * to set up the object that is being sent.  Then, when it is received, the
     * other Memory constructor that accepts a Parcel reference can "unpack it"
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(distance);
        dest.writeString(pace);
        dest.writeString(time);
        dest.writeString(name);
        dest.writeString(docID);
    }

    public static final Parcelable.Creator<Run> CREATOR = new Parcelable.Creator<Run>() {

                @Override
                public Run createFromParcel(Parcel parcel) {
                    return new Run(parcel);
                }

                @Override
                public Run[] newArray(int size) {
                    return new Run[0];
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
    public String toString(){
        return (name.toUpperCase()) + "\t\t" +date + "\nDistance: " + distance + "\t\tTime: " + time;
     }
    @Override
    public int describeContents() {
        return 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getTime(){ return time; }

    public void setTime(String time) { this.time = time; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}

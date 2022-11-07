package com.example.runbuddies;

import android.os.Parcel;
import android.os.Parcelable;



public class Run implements Parcelable {

    private String date;
    private double distance;
    private double pace;
    private double time;
    private String name;
    private String docID;

    public Run(String date, double distance, double pace, double time, String name, String docID) {
        this.date = date;
        this.distance = distance;
        this.pace = pace;
        this.time = time;
        this.name = name;
        this.docID = docID;
    }

    public Run(String date, double distance, double pace, double time, String name) {
        this.date = date;
        this.distance = distance;
        this.pace = pace;
        this.time = time;
        this.name = name;
        this.docID = "No docID yet";
    }

    public Run() {
        date = "no date";
        distance = 0;
        pace = 0;
        time = 0;
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
        distance = parcel.readDouble();
        pace = parcel.readDouble();
        time = parcel.readDouble();
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
        dest.writeDouble(distance);
        dest.writeDouble(pace);
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

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

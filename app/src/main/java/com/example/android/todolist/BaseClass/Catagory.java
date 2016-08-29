package com.example.android.todolist.BaseClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pinal on 20/08/2016.
 * Catagory class
 */
public class Catagory implements Parcelable {

    private int catagoryId;
    private String catagoryName;

    public int getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(int catagoryId) {
        this.catagoryId = catagoryId;
    }

    public String getCatagoryName() {
        return catagoryName;
    }

    public void setCatagoryName(String catagoryName) {
        this.catagoryName = catagoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.catagoryId);
        dest.writeString(this.catagoryName);
    }

    public Catagory() {
    }

    protected Catagory(Parcel in) {
        this.catagoryId = in.readInt();
        this.catagoryName = in.readString();
    }

    public static final Creator<Catagory> CREATOR = new Creator<Catagory>() {
        @Override
        public Catagory createFromParcel(Parcel source) {
            return new Catagory(source);
        }

        @Override
        public Catagory[] newArray(int size) {
            return new Catagory[size];
        }
    };
}


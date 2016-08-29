package com.example.android.todolist.BaseClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pinal on 22/08/2016.
 */
public class Task implements Parcelable {
    private int taskId;
    private String taskDetail;
    private int catagoryId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public int getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(int catagoryId) {
        this.catagoryId = catagoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskId);
        dest.writeString(this.taskDetail);
        dest.writeInt(this.catagoryId);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.taskId = in.readInt();
        this.taskDetail = in.readString();
        this.catagoryId = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

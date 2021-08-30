package com.example.todolist2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task implements Parcelable
{

    //todo модели для адаптера и БД должны быть разными

    //todo один класс для вью (адаптера), другой класс для БД

    public Task(int taskId, String taskTitle, String creationDate, String deadline, String description, boolean doneCheck) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.doneCheck = doneCheck;
        this.description = description;
    }

    @PrimaryKey(autoGenerate = true)
    public int taskId;
    @ColumnInfo(name = "title")
    public String taskTitle;
    @ColumnInfo(name = "creation_date")
    public String creationDate;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "deadline")
    public String deadline;
    @ColumnInfo(name = "doneCheck")
    public boolean doneCheck;


    protected Task(Parcel in) {
        taskId = in.readInt();
        taskTitle = in.readString();
        creationDate = in.readString();
        description = in.readString();
        deadline = in.readString();
        doneCheck = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskId);
        dest.writeString(taskTitle);
        dest.writeString(creationDate);
        dest.writeString(description);
        dest.writeString(deadline);
        dest.writeByte((byte) (doneCheck ? 1 : 0));
    }
}

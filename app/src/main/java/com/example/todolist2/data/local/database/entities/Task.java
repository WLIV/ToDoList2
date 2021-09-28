package com.example.todolist2.data.local.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task
{


    public Task(int taskId, String taskTitle, String creationDate, String deadline,
                String description, boolean doneCheck) {
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



}

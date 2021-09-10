package com.example.todolist2.features.taskList.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.todolist2.data.local.database.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskModel implements Parcelable {
    private int taskIdModel;
    private String taskTitle, creationDate, deadline,description;
    boolean doneCheck;



    public TaskModel(int taskId, String taskTitle, String creationDate, String deadline,
                     String description, boolean doneCheck) {
        this.taskIdModel = taskId;
        this.taskTitle = taskTitle;
        this.creationDate = creationDate;
        this.deadline= deadline;
        this.doneCheck = doneCheck;
        this.description = description;
    }


    protected TaskModel(Parcel in) {
        taskIdModel = in.readInt();
        taskTitle = in.readString();
        creationDate = in.readString();
        deadline = in.readString();
        description = in.readString();
        doneCheck = in.readByte() != 0;
    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            return new TaskModel(in);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };





    public int getId() {
        return taskIdModel;
    }
    public String getTaskTitle(){
        return taskTitle;
    }
    public String getTaskCreationDate(){
        return creationDate;
    }
    public String getTaskDeadline(){
        return deadline;
    }
    public String getTaskDescription(){
        return description;
    }
    public boolean getTaskStatus(){
        return doneCheck;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskIdModel);
        dest.writeString(taskTitle);
        dest.writeString(creationDate);
        dest.writeString(deadline);
        dest.writeString(description);
        dest.writeByte((byte) (doneCheck ? 1 : 0));
    }
}

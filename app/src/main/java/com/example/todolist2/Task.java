package com.example.todolist2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task
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
    //todo рум поддерживает автогенерацию id
//    @PrimaryKey(autoGenerate = true)
    @PrimaryKey
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

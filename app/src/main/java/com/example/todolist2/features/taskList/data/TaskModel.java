package com.example.todolist2.features.taskList.data;

public class TaskModel {
    private int taskIdModel;
    private String taskTitleModel, creationDateModel, deadlineModel,descriptionModel;
    boolean doneCheckModel;

    public TaskModel(int taskId, String taskTitle, String creationDate, String deadline, String description, boolean doneCheck) {
        this.taskIdModel = taskId;
        this.taskTitleModel = taskTitle;
        this.creationDateModel = creationDate;
        this.deadlineModel = deadline;
        this.doneCheckModel = doneCheck;
        this.descriptionModel = description;
    }

    public TaskModel() {

    }

    public int getId() {
        return taskIdModel;
    }
    public String getTaskTitle(){
        return taskTitleModel;
    }
    public String getTaskCreationDate(){
        return creationDateModel;
    }
    public String getTaskDeadline(){
        return deadlineModel;
    }
    public String getTaskDescription(){
        return descriptionModel;
    }
    public boolean getTaskStatus(){
        return doneCheckModel;
    }
}

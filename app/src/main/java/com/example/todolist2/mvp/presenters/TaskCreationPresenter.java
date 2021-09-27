package com.example.todolist2.mvp.presenters;

import android.content.Context;

import com.example.todolist2.R;
import com.example.todolist2.data.local.database.Database;
import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.mvp.mvmViews.TaskCreateView;

import java.util.Calendar;

public class TaskCreationPresenter {

    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int myMonth = cal.get(Calendar.MONTH);
    private String currentDate = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear);
    private TaskCreateView view;
    private Context context;
    private Database db;
    private Task chosenTask;
    public TaskCreationPresenter(Context context)
    {
        this.context = context;
        db = Database.getDbInstance(context);
    }

    public void attachView(TaskCreateView view){
        this.view = view;
    }

    public void detachView(){
        view = null;
    }

    public boolean insertData(String taskTitle, String taskDescription, String taskDeadline){
        view.showLoading();
        boolean doneCheck = false;
        if (taskTitle.isEmpty() || taskDescription.isEmpty()) {
            view.hideLoading();
            return false;
        } else {
            Task newTask = new Task(0, taskTitle, currentDate, taskDeadline, taskDescription, doneCheck);
            db.taskDao().insertAll(newTask);
            view.hideLoading();
            view.showTextMessage(context.getString(R.string.task_added));
            return true;
        }


    }


    public void setCertainTaskData(Task task) {
        view.showLoading();
         chosenTask = task;
         String title = chosenTask.taskTitle;
         String description = chosenTask.description;
         String deadline = chosenTask.deadline;
         view.setData(title, description, deadline);
         view.hideLoading();

    }

    public boolean onDateSet(int year, int month, int dayOfMonth) {
        if (year < myYear) {

            return false;
        }
        else if(year > myYear) {
            return true;
        }
        else {
            if (month < myMonth) {


                return false;

            }
            else if (month > myMonth) {
                return true;
            }
            else {
                if (dayOfMonth < myDay) {

                    return false;
                }
                else {
                    return true;
                }
            }

        }

    }

    public void updateTask(String newName, String newDescription, String newDeadline) {
        view.showLoading();
        Task updatedTask = new Task(chosenTask.taskId, newName,chosenTask.creationDate, newDeadline,newDescription, chosenTask.doneCheck);
        db.taskDao().updateAll(updatedTask);
        view.showTextMessage(context.getString(R.string.task_updated));
        view.hideLoading();
    }

    public void deleteTask() {
        view.showLoading();
        db.taskDao().delete(chosenTask);
        view.hideLoading();
        view.showTextMessage(context.getString(R.string.task_deleted));
    }

    public void setTaskDone() {
        view.showLoading();
        Task task = new Task(chosenTask.taskId, chosenTask.taskTitle, chosenTask.creationDate, chosenTask.deadline, chosenTask.description, true);
        db.taskDao().updateAll(task);
        view.hideLoading();
        view.showTextMessage(context.getString(R.string.task_set_done));

    }
}

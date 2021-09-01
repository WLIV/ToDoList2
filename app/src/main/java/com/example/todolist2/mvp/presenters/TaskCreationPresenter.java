package com.example.todolist2.mvp.presenters;

import android.content.Context;

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
    public boolean insertData(){
        String taskTitle = view.getTitleField();
        String taskDescription = view.getDescriptionField();
        String taskDeadline = view.getDateField();
        boolean doneCheck = false;
        if (taskTitle.isEmpty() || taskDescription.isEmpty())
        {
            return false;
        }
        else
            {
                Task newTask = new Task(0, taskTitle, currentDate, taskDeadline, taskDescription, doneCheck);
                db.taskDao().insertAll(newTask);
                return true;
            }


    }

    public void getCertainTask(Task task) {
         chosenTask = task;
        view.setData(chosenTask.taskTitle, chosenTask.description, chosenTask.deadline);

    }

    public boolean onDateSet(int year, int month, int dayOfMonth) {
        if (year < myYear)
        {

            return false;
        }
        else if(year > myYear)
        {
            return true;
        }
        else {
            if (month < myMonth) {


                return false;

            }
            else if (month > myMonth)
            {
                return true;
            }
            else {
                if (dayOfMonth < myDay)
                {

                    return false;
                }
                else
                {
                    return true;
                }
            }

        }

    }

    public void updateTask(String newName, String newDescription, String newDeadline) {
        Task updatedTask = new Task(chosenTask.taskId, newName,chosenTask.creationDate, newDeadline,newDescription, chosenTask.doneCheck);
        db.taskDao().updateAll(updatedTask);
    }

    public void deleteTask() {

        db.taskDao().delete(chosenTask);
    }

    public void setTaskDone() {
        Task task = new Task(chosenTask.taskId, chosenTask.taskTitle, chosenTask.creationDate, chosenTask.deadline, chosenTask.description, true);
        db.taskDao().updateAll(task);

    }
}

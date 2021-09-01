package com.example.todolist2.mvp.presenters;

import android.content.Context;

import com.example.todolist2.data.local.SharedPrefsHolder;
import com.example.todolist2.data.local.database.Database;
import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.features.taskList.data.Sort;
import com.example.todolist2.mvp.mvmViews.TasksListView;

import java.util.List;

public class TasksListPresenter {
    private List<Task> taskList;
    private Context context;
    private TasksListView view;
    private Sort sortChoice;
    private boolean hideDone;
    private Database db;
    private SharedPrefsHolder prefs;
    public static final String POSITION = "position";
    public static final String SWITCH = "switch";

    public TasksListPresenter(Context context)
    {
        this.context = context;
        db = Database.getDbInstance(context);
        prefs = new SharedPrefsHolder(context);
        hideDone = prefs.getBoolean(SWITCH);
        sortChoice = Sort.values()[prefs.getInt(POSITION)];
    }
    public int getSavedPosition() {
        int x = prefs.getInt(POSITION);
        return x;
    }
    public boolean getSavedBoolean() {
        boolean x = prefs.getBoolean(SWITCH);
        return x;
    }

    public void savePosition(int position)
    {
        prefs.setInt(POSITION, position);
    }

    public void saveSwitch(boolean x)
    {
        prefs.setBoolean(SWITCH, x);
    }
    public void getList(){
        //todo defineSort может вернуть список элементов, поле taskList не нужно
        //опять же, старайся не делать такие поля
        defineSort();
        view.showList(taskList);
    }

    public void changeSort(Sort sort, boolean hideDone){
        sortChoice = sort;
        defineSort();
        view.setSortType(sortChoice, hideDone);
    }

    public void onHideCompletedTaskPressed(boolean hideCompletedTasks){
        prefs.setBoolean(SWITCH, hideCompletedTasks);
        hideDone = hideCompletedTasks;
        view.setCompletedTasks(sortChoice, hideCompletedTasks);
    }

    public void attachView(TasksListView view){
        this.view = view;

    }

    public void detachView(){
        view = null;
    }

    private void defineSort()
    {
        switch (sortChoice) {
            case CREATION_DATE:
                loadTaskList();
                break;
            case DEADLINE:
                sortByDeadline();
                break;
            case STATUS:
                sortByDone();
                break;
            case TITLE:
                sortByTitle();
                break;
        }

    }



    private void sortByTitle()
    {

        if (!hideDone) {

            taskList = db.taskDao().orderByTitle();

        }
        else {

            taskList = db.taskDao().orderByTitleDone(false);

        }


    }

    private void sortByDeadline()
    {

        if (!hideDone) {

            taskList = db.taskDao().orderByDeadline();

        }
        else{

            taskList = db.taskDao().orderByDeadlineDone(false);

        }

    }

    private void sortByDone()
    {

        if (!hideDone) {

            taskList = db.taskDao().orderByDone();

        }
        else {

            taskList = db.taskDao().orderByDoneDone(false);

        }



    }
    private void loadTaskList()
    {

        if (!hideDone) {
            taskList = db.taskDao().getAll();

        }
        else{
            taskList = db.taskDao().getAllDone(false);
        }
    }
}

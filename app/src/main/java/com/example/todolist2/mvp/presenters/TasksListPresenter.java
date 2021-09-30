package com.example.todolist2.mvp.presenters;

import android.content.Context;
import android.os.AsyncTask;

import com.example.todolist2.convertors.TasksConverter;
import com.example.todolist2.data.local.SharedPrefsHolder;
import com.example.todolist2.data.local.database.Database;
import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.features.taskList.data.Sort;
import com.example.todolist2.mvp.mvmViews.TasksListView;

import java.util.List;

public class TasksListPresenter {
    private Context context;
    private TasksListView view;
    private Sort sortChoice;
    private boolean hideDone;
    private Database db;
    private SharedPrefsHolder prefs;
    public static final String POSITION = "position";
    public static final String SWITCH = "switch";
    private MyAsyncTask listTask;

    public TasksListPresenter(Context context) {

        this.context = context;
        listTask = new MyAsyncTask();
        db = Database.getDbInstance(context);
        prefs = new SharedPrefsHolder(context);
        hideDone = prefs.getBoolean(SWITCH);
        sortChoice = Sort.values()[prefs.getInt(POSITION)];
    }
    public int getSavedPosition() {

        return prefs.getInt(POSITION);
    }


    public boolean getSwitchSavedPostion() {

        return prefs.getBoolean(SWITCH);
    }

    public void savePosition(int position) {
        prefs.setInt(POSITION, position);
    }

    public void saveSwitch(boolean x) {
        prefs.setBoolean(SWITCH, x);
    }
    public void getList() {

        if (listTask != null && listTask.getStatus() == AsyncTask.Status.RUNNING)
            return;

        listTask = new MyAsyncTask();
        listTask.execute();

    }

    public void changeSort(Sort sort, boolean hideDone) {
        sortChoice = sort;
        view.setSortType( hideDone);
        getList();
    }

    public void onHideCompletedTaskPressed(boolean hideCompletedTasks) {
        prefs.setBoolean(SWITCH, hideCompletedTasks);
        hideDone = hideCompletedTasks;
        changeSort(sortChoice, hideCompletedTasks);
        saveSwitch(hideCompletedTasks);
        view.setCompletedTasks(hideCompletedTasks);
        getList();
    }

    public void attachView(TasksListView view) {
        this.view = view;

    }

    public void detachView() {
        listTask = null;
        view = null;
    }

    private List<Task> defineSort() {
        List<Task> taskList;
        switch (sortChoice) {
            default:
                taskList = loadTaskList();
                break;
            case CREATION_DATE:
                taskList = loadTaskList();
                break;
            case DEADLINE:
                taskList = sortByDeadline();
                break;
            case STATUS:
                taskList = sortByDone();
                break;
            case TITLE:
                taskList = sortByTitle();
                break;
        }
        return taskList;
    }



    private List<Task> sortByTitle() {
        List<Task> taskList;
        if (!hideDone) {

            taskList = db.taskDao().orderByTitle();

        }
        else {

            taskList = db.taskDao().orderByTitleDone(false);

        }
        return taskList;

    }

    private List<Task> sortByDeadline() {
        List<Task> taskList;
        if (!hideDone) {

            taskList = db.taskDao().orderByDeadline();

        }
        else{

            taskList = db.taskDao().orderByDeadlineDone(false);

        }
        return taskList;
    }

    private List<Task> sortByDone() {
        List<Task> taskList;
        if (!hideDone) {

            taskList = db.taskDao().orderByDone();

        }
        else {

            taskList = db.taskDao().orderByDoneDone(false);

        }

        return taskList;

    }
    private List<Task> loadTaskList() {
        List<Task> taskList;
        if (!hideDone) {
            taskList = db.taskDao().getAll();

        }
        else{
            taskList = db.taskDao().getAllDone(false);
        }
        return taskList;
    }


    class MyAsyncTask extends AsyncTask<Void, List<Task>, List<Task>> {

        @Override
        protected void onPreExecute() {
            view.showLoading();
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {
           List<Task> taskList = defineSort();
            return taskList;
        }

        @Override
        protected void onPostExecute(List<Task> taskList) {
            super.onPostExecute(taskList);
            view.showList(TasksConverter.taskListToTaskModelList(taskList));
            view.hideLoading();
            listTask = null;
        }
    }
}


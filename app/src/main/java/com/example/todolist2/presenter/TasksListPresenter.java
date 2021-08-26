package com.example.todolist2.presenter;

import com.example.todolist2.MainFragment;
import com.example.todolist2.mvmViews.TasksListView;

public class TasksListPresenter {

    private TasksListView view;

    //todo контекст передать в конструкторе презентера
//    private SharedPrefsHolder prefs = SharedPrefsHolder();

//    private Database db = Database.getDbInstance()

    public void getList(){
        //todo запрос списка из БД
//        view.showList(
//
//        );
    }

    public void changeSort(MainFragment.Sort sort){
        //todo смену сортировки
        //prefs.setInt(sort.position)
    }

    public void onHideCompletedTaskPressed(boolean hideCompletedTasks){
        //todo смена свича
        //prefs.setBoolean(hideCompletedTasks)
        view.setCompletedTasks(hideCompletedTasks);
    }

    public void attachView(TasksListView view){
        this.view = view;
    }

    public void detachView(){
        view = null;
    }

}

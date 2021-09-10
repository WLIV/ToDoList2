package com.example.todolist2.mvp.mvmViews;

public interface TaskCreateView extends MvpVIew{

    public void showInvalidDateDialog();
    void setData(String title, String description, String date);
}

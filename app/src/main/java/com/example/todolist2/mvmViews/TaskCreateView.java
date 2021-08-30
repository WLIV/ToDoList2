package com.example.todolist2.mvmViews;

import android.content.Context;

public interface TaskCreateView extends MvpVIew{
    public String getTitleField();
    public String getDescriptionField();
    public String getDateField();
    public void showInvalidDateDialog();
    void setData(String title, String description, String date);
}

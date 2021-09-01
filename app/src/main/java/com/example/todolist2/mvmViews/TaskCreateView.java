package com.example.todolist2.mvmViews;

public interface TaskCreateView extends MvpVIew{
    //todo тут не должно быть методов получения данных
    //только методы для смены состояния вью
    //данные передаем в параментрах методов презентера, как это сделано в updateTask в классе TaskCreationPresenter
    public String getTitleField();
    public String getDescriptionField();
    public String getDateField();

    public void showInvalidDateDialog();
    void setData(String title, String description, String date);
}

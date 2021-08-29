package com.example.todolist2.data;

//todo лучше вынести в отдельный файл
public enum Sort{

    //todo остальные типы
    CREATION_DATE(0),
    DEADLINE(1),
    STATUS(2),
    TITLE(3);


    public final int position;

    private Sort(int position){
        this.position = position;
    }

}
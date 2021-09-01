package com.example.todolist2.data;

public enum Sort{

    CREATION_DATE(0),
    DEADLINE(1),
    STATUS(2),
    TITLE(3);


    public final int position;

    private Sort(int position){
        this.position = position;
    }

}
package com.example.todolist2.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHolder {

    private final SharedPreferences sharedPreferences;

    private final String TEXT = "sharedPrefs";

    public SharedPrefsHolder(Context context){
        sharedPreferences = context.getSharedPreferences(TEXT, Context.MODE_PRIVATE);
    }

    public void setString(String key, String value){
        //todo
    }

    //todo boolean, int, long

//    public String getString(String key, String defaultValue){
//        //todo
//    }

}

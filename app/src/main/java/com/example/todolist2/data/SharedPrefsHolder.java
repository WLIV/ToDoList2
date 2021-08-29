package com.example.todolist2.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHolder {

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Context context;
    private final String TEXT = "sharedPrefs";

    public SharedPrefsHolder(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(TEXT, Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
    }

    public void setInt(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }

    public void setBoolean(String key, boolean value)
    {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key)
    {
        int value;
        value = sharedPreferences.getInt(key, 0);
        return value;
    }
    public boolean getBoolean(String key)
    {
        boolean value;
        value = sharedPreferences.getBoolean(key, false);
        return value;
    }

}

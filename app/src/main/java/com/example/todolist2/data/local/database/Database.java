package com.example.todolist2.data.local.database;


import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todolist2.data.local.database.dao.TaskDao;
import com.example.todolist2.data.local.database.entities.Task;

@androidx.room.Database(entities = {Task.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract TaskDao taskDao();
    private static Database instance;

    public static Database getDbInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "database").allowMainThreadQueries().build();
        }
        return instance;
    }
}

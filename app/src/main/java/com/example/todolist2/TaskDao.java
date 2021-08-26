package com.example.todolist2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao
{
    @Query("SELECT * FROM task")
    List<Task>getAll();

    @Query("SELECT * FROM task WHERE title LIKE :name")
    Task findByTitle(String name);

    @Query("SELECT * FROM task ORDER BY doneCheck")
    List<Task>orderByDone();

    @Query("SELECT * FROM task ORDER BY deadline")
    List<Task>orderByDeadline();

    @Query("SELECT * FROM task ORDER BY title")
    List<Task>orderByTitle();

    @Query("SELECT * FROM task WHERE doneCheck LIKE :done ORDER BY doneCheck")
    List<Task>orderByDoneDone(boolean done);

    @Query("SELECT * FROM task WHERE doneCheck LIKE :done ORDER BY deadline")
    List<Task>orderByDeadlineDone(boolean done);

    @Query("SELECT * FROM task WHERE doneCheck LIKE :done ORDER BY title")
    List<Task>orderByTitleDone(boolean done);

    @Query("SELECT * FROM task WHERE doneCheck LIKE :done")
    List<Task>getAllDone(boolean done);

    @Query("DELETE FROM task")
    void deleteAll();


    @Insert
    void insertAll(Task ... tasks);

    @Delete
    void delete (Task task);

    @Update
    void updateAll(Task task);
}

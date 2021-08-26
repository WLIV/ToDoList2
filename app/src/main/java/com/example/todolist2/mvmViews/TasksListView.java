package com.example.todolist2.mvmViews;

import com.example.todolist2.MainFragment;
import com.example.todolist2.Task;

import java.util.List;

public interface TasksListView extends MvpVIew {

    void showList(List<Task> tasks);

    void setSortType(MainFragment.Sort sort);

    void setCompletedTasks(boolean showCompletedTasks);

}

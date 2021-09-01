package com.example.todolist2.mvp.mvmViews;

import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.features.taskList.data.Sort;

import java.util.List;

public interface TasksListView extends MvpVIew {

    void showList(List<Task> tasks);

    void setSortType(Sort sort, boolean hideDone);

    void setCompletedTasks(Sort sort, boolean showCompletedTasks);

}

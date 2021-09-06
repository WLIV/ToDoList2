package com.example.todolist2.mvp.mvmViews;

import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.features.taskList.data.Sort;
import com.example.todolist2.features.taskList.data.TaskModel;

import java.util.List;

public interface TasksListView extends MvpVIew {

    void showList(List<TaskModel> tasks);

    void setSortType( boolean hideDone);

    void setCompletedTasks( boolean showCompletedTasks);

}

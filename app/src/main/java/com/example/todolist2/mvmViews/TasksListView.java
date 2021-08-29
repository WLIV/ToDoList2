package com.example.todolist2.mvmViews;

import com.example.todolist2.MainFragment;
import com.example.todolist2.Task;
import com.example.todolist2.data.Sort;

import java.util.List;

public interface TasksListView extends MvpVIew {

    void showList(List<Task> tasks);

    void setSortType(Sort sort, boolean hideDone);

    void setCompletedTasks(Sort sort, boolean showCompletedTasks);

}

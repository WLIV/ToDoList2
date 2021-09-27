package com.example.todolist2.convertors;

import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.features.taskList.data.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TasksConverter {

    //fixme у тебя тут обратная конвертация: taskToModel
    public static TaskModel taskModelToTask (Task task) {
        return new TaskModel(task.taskId, task.taskTitle, task.creationDate,
                task.deadline, task.description, task.doneCheck);

    }

    public static List<TaskModel> taskToTaskModel(List<Task> taskList){
        List<TaskModel> taskListModel = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            //fixme у тебя уже есть готовая функция taskToModel, вызывай ее в цикле
            Task task = taskList.get(i);
            TaskModel taskModel = new TaskModel(task.taskId, task.taskTitle, task.creationDate,
                    task.deadline, task.description, task.doneCheck);
            taskListModel.add(taskModel);
        }
        return taskListModel;
    }
    public static Task toTask (TaskModel taskModel) {
        return new Task(taskModel.getId(), taskModel.getTaskTitle(),
                taskModel.getTaskCreationDate(), taskModel.getTaskDeadline(),
                taskModel.getTaskDescription(), taskModel.getTaskStatus());
    }
}

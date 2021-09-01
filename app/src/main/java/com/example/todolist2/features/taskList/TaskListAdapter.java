package com.example.todolist2.features.taskList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist2.R;
import com.example.todolist2.data.local.database.entities.Task;

import java.util.List;

//todo переименовать в TaskListAdapter
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder>
{
    private TaskListener mTaskListener;
    private Context context;
    private List<Task> taskList;

    public TaskListAdapter(Context context, TaskListener onTaskListener)
    {
        this.context = context;
        this.mTaskListener = onTaskListener;
    }

    //todo слушатель передавать в кнострукторе
    //todo передавать список из TaskModel
    public void setTaskList(List<Task> taskList)
    {
        this.taskList = taskList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_row_item, parent, false);
        return new MyViewHolder(view, mTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.MyViewHolder holder, int position) {
        holder.bind(taskList.get(position));
    }

    public interface TaskListener
    {
   void onTaskClick(Task task);
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
    //todo старайся придерживаться стиля джавы, первая фигурная скобка должна быть на одной строке
    //пример на 57 строке
    //исправь везде где увидишь
    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TaskListener TaskListener;
        TextView tvDoneCheck, tvTitle, tvDescription, tvDeadline, tvCreationDate;
        public MyViewHolder(View view, TaskListener TaskListener)
        {

            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvCreationDate = view.findViewById(R.id.tvCreationDate);
            tvDeadline = view.findViewById(R.id.tvDeadline);
            tvDoneCheck = view.findViewById(R.id.tvDoneCheck);
            this.TaskListener = TaskListener;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskListener.onTaskClick(taskList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Task task){
            tvCreationDate.setText(task.creationDate);
            tvDeadline.setText(task.deadline);
            if (!task.doneCheck) {
                tvDoneCheck.setText(R.string.in_progress);
            }
            else {
               tvDoneCheck.setText(R.string.done);
            }
           tvDescription.setText(task.description);
            tvTitle.setText(task.taskTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   TaskListener.onTaskClick(task);
                }
            });
        }


        //todo удалить, больше не нужно
        @Override
        public void onClick(View view)
        {
            TaskListener.onTaskClick(taskList.get(getAdapterPosition()));
        }

    }
}

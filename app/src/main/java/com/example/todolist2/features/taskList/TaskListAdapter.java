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
import com.example.todolist2.features.taskList.data.TaskModel;

import java.util.List;
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {
    private TaskListener mTaskListener;
    private Context context;
    private List<TaskModel> taskList;

    public TaskListAdapter(Context context, TaskListener onTaskListener) {
        this.context = context;
        this.mTaskListener = onTaskListener;
    }

    public void setTaskList(List<TaskModel> taskList) {
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

    public interface TaskListener {
        void onTaskClick(TaskModel task);
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder {

        TaskListener TaskListener;
        TextView tvDoneCheck, tvTitle, tvDescription, tvDeadline, tvCreationDate;
        public MyViewHolder(View view, TaskListener TaskListener) {

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

        public void bind(TaskModel task){
            tvCreationDate.setText(task.getTaskCreationDate());
            tvDeadline.setText(task.getTaskDeadline());
            if (!task.getTaskStatus()) {
                tvDoneCheck.setText(R.string.in_progress);
            }
            else {
               tvDoneCheck.setText(R.string.done);
            }
            tvDescription.setText(task.getTaskDescription());
            tvTitle.setText(task.getTaskTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   TaskListener.onTaskClick(task);
                }
            });
        }
    }
}

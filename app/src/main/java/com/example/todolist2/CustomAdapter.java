package com.example.todolist2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//todo переименовать в TaskListAdapter
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>
{
    private onTaskListener monTaskListener;
    private Context context;
    private List<Task> taskList;

    public CustomAdapter(Context context, onTaskListener onTaskListener)
    {
        this.context = context;
        this.monTaskListener = onTaskListener;
    }

    //todo слушатель передовать в кнострукторе
    public void setTaskList(List<Task> taskList)
    {
        this.taskList = taskList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_row_item, parent, false);
        return new MyViewHolder(view, monTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        //todo привязка значний к полям должна происходить в классе ViewHolder
        holder.tvCreationDate.setText(this.taskList.get(position).creationDate);
        holder.tvDeadline.setText(this.taskList.get(position).deadline);
//        holder.bind(taskList[position]);
        if (!this.taskList.get(position).doneCheck) {
            holder.tvDoneCheck.setText("In progress");
        }
        else {
            holder.tvDoneCheck.setText("Done");
        }
        holder.tvDescription.setText(this.taskList.get(position).description);
        holder.tvTitle.setText(this.taskList.get(position).taskTitle);
    }
    //todo переименовать в TaskListener
    public interface onTaskListener
    {
        //todo передавать не позицию, а элемент целиком
        void onTaskClick(int position);
//        void onTaskClick(Task task);
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener
    {
        onTaskListener onTaskListener;
        TextView tvDoneCheck, tvTitle, tvDescription, tvDeadline, tvCreationDate;
        public MyViewHolder(View view, onTaskListener onTaskListener)
        {

            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvCreationDate = view.findViewById(R.id.tvCreationDate);
            tvDeadline = view.findViewById(R.id.tvDeadline);
            tvDoneCheck = view.findViewById(R.id.tvDoneCheck);
            this.onTaskListener = onTaskListener;
            //todo переделать на анонимный класс, чтобы можно было перередать привязываемый элемент целиком
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTaskListener.onTaskClick(getAdapterPosition());
                }
            });
        }

        //todo
        public void bind(Task task){
            //todo
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onTaskListener.onTaskClick(task);
                }
            });
        }


        @Override
        public void onClick(View view)
        {
            onTaskListener.onTaskClick(getAdapterPosition());
        }

    }
}

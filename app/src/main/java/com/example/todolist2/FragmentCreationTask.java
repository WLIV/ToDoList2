package com.example.todolist2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import java.util.Calendar;
import java.util.List;

public class FragmentCreationTask extends Fragment {

//    сделаем это вместе
    //todo попробовать разделить по разным классам,
    // сейчас фрагмент и отображает данные,
    // и записывает/читает из БД, и содержит какуе-ту логику


    //todo вынести все строки в ресурсы

    private String resultCheck = "";
    private DatePickerDialog.OnDateSetListener setListener;
    private TextView date;
    private Button addNewTaskBtn, cancelBtn, doneBtn, deleteBtn;
    private EditText description, title;
    private View view;
    private Calendar cal = Calendar.getInstance();
    //jhgjhgjfg
    private int myYear = cal.get(Calendar.YEAR);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int myMonth = cal.get(Calendar.MONTH);
    private String dateSt = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear); //В будущем будет датой когда задача должна быть выполнена
    private String currentDate = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear); //Текущая дата для определения времени когда задача была создана

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_creation_task, container, false);
        getParentFragmentManager().setFragmentResultListener("taskTitle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                resultCheck = result.getString("df1");
                editTask(resultCheck);

            }
        });
        if (resultCheck.isEmpty()) {
            init();
        } else {

        }

        return view;
    }

    private void showInvalidDateDialog(){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
        a_builder.setMessage(getString(R.string.invalid_date_description)).setCancelable(false).setPositiveButton(getString(R.string.change_it), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                showDialog();
            }
        });

        AlertDialog alert = a_builder.create();
        alert.setTitle(getString(R.string.invalid_date_title));
        alert.show();
    }
    public void init() {
        date = view.findViewById(R.id.date_tv);
        addNewTaskBtn = view.findViewById(R.id.add_new_task);
        cancelBtn = view.findViewById(R.id.backButton2);
        date.setText(dateSt);
        description = view.findViewById(R.id.description_text);
        title = view.findViewById(R.id.title_input);
        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, myYear, myMonth, myDay
                    );
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            }
        });


        setListener = new DatePickerDialog.OnDateSetListener() {
            //todo большие функции надо дробить на маленькие
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (year < myYear)
                {
                    showInvalidDateDialog();
                }
                else if(year > myYear)
                {
                    myYear = year;
                    myMonth = month;
                    myDay = dayOfMonth;
                    dateSt = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear);
                    date.setText(dateSt);
                }
                else {
                    if (month < myMonth) {

                        showInvalidDateDialog();

                    }
                    else if (month > myMonth)
                    {
                        myYear = year;
                        myMonth = month;
                        myDay = dayOfMonth;
                        dateSt = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear);
                        date.setText(dateSt);
                    }
                    else {
                        if (dayOfMonth < myDay)
                        {
                            showInvalidDateDialog();
                        }
                        else
                        {
                            myYear = year;
                            myMonth = month;
                            myDay = dayOfMonth;
                            dateSt = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear);
                            date.setText(dateSt);
                        }
                    }

                }
            }
        };
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
            }
        });
        addNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleSt, descriptionSt;
                titleSt = title.getText().toString();
                descriptionSt = description.getText().toString();
                Database db = Database.getDbInstance(getContext());
                List<Task> taskList = db.taskDao().getAll();
                if (titleSt.isEmpty() || descriptionSt.isEmpty()){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                    a_builder.setMessage("All of the fields should be filled in").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Please fill in the gaps");
                    alert.show();
                }
                else {
                    Task newTask = new Task(taskList.size(), titleSt, currentDate, date.getText().toString(), descriptionSt, false);
                    db.taskDao().insertAll(newTask);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
                }
            }
        });
    }

    public void showDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, myYear, myMonth, myDay
        );
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    public void editTask(String nameOfTask) {
        addNewTaskBtn = view.findViewById(R.id.add_new_task);
        addNewTaskBtn.setText("Save changes");
        Database db = Database.getDbInstance(getContext());
        Task task = db.taskDao().findByTitle(nameOfTask);
        description.setText(task.description);
        title.setText(task.taskTitle);
        date.setText(task.deadline);
        doneBtn = view.findViewById(R.id.done_button);
        deleteBtn = view.findViewById(R.id.delete_button);
        deleteBtn.setBackgroundColor(Color.RED);
        deleteBtn.setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
        doneBtn.setBackgroundColor(Color.GREEN);
        doneBtn.setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
        addNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = Database.getDbInstance(getContext());
                String newName, newDescription, newDeadline;
                newName = title.getText().toString();
                newDescription = description.getText().toString();
                newDeadline = date.getText().toString();
                if (newName.isEmpty() || newDescription.isEmpty()){
                    final AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                    a_builder.setMessage("All of the fields should be filled in").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    });

                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Please fill in the gaps");
                    alert.show();
                }
                else {
                    Task updatedTask = new Task(task.taskId, newName, task.creationDate, newDeadline, newDescription, task.doneCheck);
                    db.taskDao().updateAll(updatedTask);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                a_builder.setMessage("Are you sure you want to delete this task?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Database db = Database.getDbInstance(getContext());
                        db.taskDao().delete(task);
                        Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                a_builder.show();

            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = Database.getDbInstance(getContext());
                Task doneTask = new Task(task.taskId, task.taskTitle, task.creationDate, task.deadline,task.description, true);
                db.taskDao().updateAll(doneTask);
                Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
            }
        });
    }
}

package com.example.todolist2;

import android.app.DatePickerDialog;
import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.todolist2.mvmViews.TaskCreateView;
import com.example.todolist2.presenter.TaskCreationPresenter;

import java.util.Calendar;
import java.util.List;

public class FragmentCreationTask extends Fragment implements TaskCreateView {

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showTextMessage(String text) {

    }

    @Override
    public String getTitleField() {
        EditText title = view.findViewById(R.id.title_input);
        return title.getText().toString();
    }

    @Override
    public String getDescriptionField() {
        EditText description = view.findViewById(R.id.description_text);
        return description.getText().toString();
    }

    @Override
    public String getDateField() {
        TextView date = view.findViewById(R.id.date_tv);
        return date.getText().toString();
    }

    @Override
    public void showInvalidDateDialog()
    {
        a_builder = new AlertDialog.Builder(getContext());
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

    @Override
    public void setData(String titleSt, String descriptionSt, String dateSt) {
        description.setText(descriptionSt);
        title.setText(titleSt);
        date.setText(dateSt);
    }


    public void showFillInTheGapsDialog(){
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
        a_builder.setMessage(R.string.missing_field).setCancelable(false).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });

        AlertDialog alert = a_builder.create();
        alert.setTitle(R.string.please_fill_in);
        alert.show();
    }

    private  AlertDialog.Builder a_builder;
    private TaskCreationPresenter presenter;
    private Task chosenTask;
    private DatePickerDialog.OnDateSetListener setListener;
    private TextView date;
    private Button addNewTaskBtn, cancelBtn, doneBtn, deleteBtn;
    private EditText description, title;
    private View view;
    private Calendar cal = Calendar.getInstance();
    private int myYear = cal.get(Calendar.YEAR);
    private int myDay = cal.get(Calendar.DAY_OF_MONTH);
    private int myMonth = cal.get(Calendar.MONTH);
    private String dateSt = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear); //В будущем будет датой когда задача должна быть выполнена
    private String currentDate = Integer.toString(myDay) + "/" + Integer.toString(myMonth + 1) + "/" + Integer.toString(myYear); //Текущая дата для определения времени когда задача была создана

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new TaskCreationPresenter(getContext());
        presenter.attachView(this);
        view = inflater.inflate(R.layout.fragment_creation_task, container, false);
        getParentFragmentManager().setFragmentResultListener("chosenTask", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                chosenTask = result.getParcelable("chosenTaskContent");
                editTask(chosenTask);

            }
        });
        if (chosenTask == null) {
            init();
        } else {

        }

        return view;
    }
    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
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
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               boolean validDate = presenter.onDateSet(year, month, dayOfMonth);
                System.out.println(validDate);
               if (validDate)
               {
                   dateSt = Integer.toString(dayOfMonth) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(year);
                   date.setText(dateSt);
               }
               else {
                   showInvalidDateDialog();
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
                boolean check = presenter.insertData();
                if (!check){
                   showFillInTheGapsDialog();
                }
                else {
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

    public void editTask(Task chosenTask) {
        addNewTaskBtn = view.findViewById(R.id.add_new_task);
        addNewTaskBtn.setText("Save changes");
        presenter.getCertainTask(chosenTask);
        doneBtn = view.findViewById(R.id.done_button);
        deleteBtn = view.findViewById(R.id.delete_button);
        deleteBtn.setBackgroundColor(Color.RED);
        deleteBtn.setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
        doneBtn.setBackgroundColor(Color.GREEN);
        doneBtn.setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
        addNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName, newDescription, newDeadline;
                newName = title.getText().toString();
                newDescription = description.getText().toString();
                newDeadline = date.getText().toString();
                if (newName.isEmpty() || newDescription.isEmpty()){
                    showFillInTheGapsDialog();
                }
                else {
                    presenter.updateTask(newName, newDescription, newDeadline);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder a_builder = new AlertDialog.Builder(getContext());
                a_builder.setMessage(R.string.are_you_sure).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        presenter.deleteTask();
                        Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
                presenter.setTaskDone();
                Navigation.findNavController(view).navigate(R.id.action_fragmentCreationTask_to_mainFragment);
            }
        });
    }


}

package com.example.todolist2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist2.data.Sort;
import com.example.todolist2.mvmViews.TasksListView;
import com.example.todolist2.presenter.TasksListPresenter;

import java.util.List;


public class MainFragment extends Fragment implements CustomAdapter.onTaskListener, TasksListView {
    private Button createNewTaskBtn;
    //todo плохой синтаксис, в джаве не используется _ в названии полей и классов
    private Spinner spinnerSort;
    private View view;
    private CustomAdapter customAdapter;
    private static final String TEXT = "sharedPrefs";
    public static final String POSITION = "position";
    public static final String SWITCH = "switch";
    //todo enum Sort
    private int chosenPosition;
    private Switch hideDoneSwitch;
    private boolean hideDone;
    private List<Task> taskList;


    private TasksListPresenter presenter;

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
    public void showList(List<Task> tasks) {

        this.taskList = tasks;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        customAdapter = new CustomAdapter(getContext(), this);
        customAdapter.setTaskList(tasks);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void setSortType(Sort sort, boolean hideDone) {
       spinnerSort.setSelection(chosenPosition);
       hideDoneSwitch.setChecked(hideDone);
       presenter.getList();
    }

    @Override
    public void setCompletedTasks(Sort sort, boolean showCompletedTasks) {
        hideDoneSwitch.setChecked(showCompletedTasks);
        presenter.changeSort(sort, showCompletedTasks);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        presenter =  new TasksListPresenter(getContext()); //todo передать контекст
        presenter.attachView(this);
        presenter.getList();
        chosenPosition = presenter.getSavedPosition();
        hideDone = presenter.getSavedBoolean();

        init();



        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }



    private void init()
    {
        hideDoneSwitch = view.findViewById(R.id.hide_switch);
        hideDoneSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHideCompletedTaskPressed(hideDoneSwitch.isChecked());
                presenter.getList();
            }
        });
        spinnerSort = view.findViewById(R.id.spinner_sort);
        String[] sortOption = {"Creation Date", "Deadline", "Status", "Title"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortOption);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setSelection(chosenPosition);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenPosition = position;
                presenter.savePosition(position);
                Sort sort = Sort.values()[position];
                presenter.changeSort(sort, hideDone);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerSort.setOnItemSelectedListener(itemSelectedListener);
        createNewTaskBtn = view.findViewById(R.id.create_new);
        createNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentCreationTask);
            }
        });
    }




    @Override
    public void onTaskClick(int position) {
        Task task = taskList.get(position);
        String clickName = task.taskTitle;
        //todo переделать на аргументы
        Bundle result = new Bundle();
        //todo df1 является константой, ее необходимо держать где-то в одном месте,
        // ну и нужно дать более осмысленное название
        result.putString("df1",clickName);
        getParentFragmentManager().setFragmentResult("taskTitle", result);
        //todo аргументы в android navigation component
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentCreationTask);
    }

}
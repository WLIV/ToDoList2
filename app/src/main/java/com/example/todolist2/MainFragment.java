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

import com.example.todolist2.mvmViews.TasksListView;
import com.example.todolist2.presenter.TasksListPresenter;

import java.util.List;


public class MainFragment extends Fragment implements CustomAdapter.onTaskListener, TasksListView {
    private Button createNewTaskBtn;
    //todo плохой синтаксис, в джаве не используется _ в названии полей и классов
    private Spinner spinner_sort;
    private View view;
    private CustomAdapter customAdapter;
    private static final String TEXT = "sharedPrefs";
    public static final String POSITION = "position";
    public static final String SWITCH = "switch";
    //todo enum Sort
    private int chosenPosition;
    private Switch hideDoneSwitch;

    boolean hideDone;

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
        //adapter.setList(tasks)
    }

    @Override
    public void setSortType(Sort sort) {
        //spinner.setPosition(sort.position)
    }

    @Override
    public void setCompletedTasks(boolean showCompletedTasks) {
        //switch.isEnabled = showCompletedTasks
    }

    //todo лучше вынести в отдельный файл
    public enum Sort{

        //todo остальные типы
        NONE(0),
        DEADLINE(1);

        public final int position;

        private Sort(int position){
            this.position = position;
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        presenter =  new TasksListPresenter(); //todo передать контекст
        presenter.attachView(this);
        loadData();
        init();
        initRecyclerView();
        defineSort();


        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    private void defineSort()
    {
            switch (chosenPosition) {
                case 0:
                    loadTaskList();
                    break;
                case 1:
                    sortByDeadline();
                    break;
                case 2:
                    sortByDone();
                    break;
                case 3:
                    sortByTitle();
                    break;
            }

    }



    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        customAdapter = new CustomAdapter(getContext());
        recyclerView.setAdapter(customAdapter);
    }

    private void init()
    {
        hideDoneSwitch = view.findViewById(R.id.hide_switch);
        hideDoneSwitch.setChecked(hideDone);
        hideDoneSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDone = hideDoneSwitch.isChecked();
                saveData();
                defineSort();
            }
        });
        spinner_sort = view.findViewById(R.id.spinner_sort);
        String[] sortOption = {"Creation Date", "Deadline", "Status", "Title"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortOption);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sort.setAdapter(adapter);
        spinner_sort.setSelection(chosenPosition);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenPosition = position;
                saveData();
                defineSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner_sort.setOnItemSelectedListener(itemSelectedListener);
        createNewTaskBtn = view.findViewById(R.id.create_new);
        createNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentCreationTask);
            }
        });
    }

    private void sortByTitle()
    {
        Database db = Database.getDbInstance(getContext());
        List<Task> taskList;
        if (!hideDone) {
            taskList = db.taskDao().orderByTitle();
        }
        else {
            taskList = db.taskDao().orderByTitleDone(false);
        }
        customAdapter.setTaskList(taskList,this);

    }

    private void sortByDeadline()
    {
        Database db = Database.getDbInstance(getContext());
        List<Task> taskList;
        if (!hideDone) {
            taskList = db.taskDao().orderByDeadline();
        }
        else{
            taskList = db.taskDao().orderByDeadlineDone(false);
        }
        customAdapter.setTaskList(taskList, this);
    }

    private void sortByDone()
    {
        Database db = Database.getDbInstance(getContext());
        List<Task> taskList;
        if (!hideDone) {
            taskList = db.taskDao().orderByDone();
        }
        else {
            taskList = db.taskDao().orderByDoneDone(false);
        }
        customAdapter.setTaskList(taskList,this);


    }
    private void loadTaskList()
    {
        Database db = Database.getDbInstance(getContext());
        if (!hideDone) {
            List<Task> taskList = db.taskDao().getAll();
            customAdapter.setTaskList(taskList, this);
        }
        else{
            List<Task> taskList = db.taskDao().getAllDone(false);
            customAdapter.setTaskList(taskList, this);
        }
    }


    @Override
    public void onTaskClick(int position) {
        Database db = Database.getDbInstance(this.getContext());
        List<Task> taskList = db.taskDao().getAll();
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

    public void saveData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(TEXT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(POSITION, chosenPosition);
        editor.putBoolean(SWITCH, hideDoneSwitch.isChecked());
        editor.apply();



    }

    private void loadData()
    {
        //todo сделать отдельный класс с sharedPreferences с методами getStrting, setString, getBoolean и т.д
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(TEXT, Context.MODE_PRIVATE);
        chosenPosition = sharedPreferences.getInt(POSITION, 0);
        hideDone = sharedPreferences.getBoolean(SWITCH, false);


    }
}
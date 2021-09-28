package com.example.todolist2.features.taskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist2.R;
import com.example.todolist2.features.taskList.data.Sort;
import com.example.todolist2.features.taskList.data.TaskModel;
import com.example.todolist2.mvp.mvmViews.TasksListView;
import com.example.todolist2.mvp.presenters.TasksListPresenter;

import java.util.List;


public class MainFragment extends Fragment implements TaskListAdapter.TaskListener, TasksListView {
    private Button createNewTaskBtn;
    private Spinner spinnerSort;
    private View view;
    private TaskListAdapter customAdapter;
    private RecyclerView recyclerView;
    private Switch hideDoneSwitch;
    private ProgressBar progressBar;
    private List<TaskModel> taskModels;


    private TasksListPresenter presenter;

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTextMessage(String text) {
        Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void showList(List<TaskModel> tasks) {
        customAdapter.setTaskList(tasks);

    }

    @Override
    public void setSortType( boolean hideDone) {
       hideDoneSwitch.setChecked(hideDone);
    }

    @Override
    public void setCompletedTasks( boolean showCompletedTasks) {
        hideDoneSwitch.setChecked(showCompletedTasks);

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        hideDoneSwitch = view.findViewById(R.id.hide_switch);
        spinnerSort = view.findViewById(R.id.spinner_sort);
        createNewTaskBtn = view.findViewById(R.id.create_new);
        presenter =  new TasksListPresenter(getContext());
        presenter.attachView(this);
        customAdapter = new TaskListAdapter(getContext(), this);
        presenter.getList();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(customAdapter);
        init();








        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }



    private void init() {


        hideDoneSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHideCompletedTaskPressed(hideDoneSwitch.isChecked());
            }
        });

        String[] sortOption = {"Creation Date", "Deadline", "Status", "Title"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortOption);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setSelection(presenter.getSavedPosition());

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                presenter.savePosition(position);
                Sort sort = Sort.values()[position];
                presenter.changeSort(sort, presenter.getSwitchSavedPostion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerSort.setOnItemSelectedListener(itemSelectedListener);
        createNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentCreationTask);
            }
        });
    }




    @Override
    public void onTaskClick(TaskModel task) {
        showLoading();
        NavDirections action = MainFragmentDirections.actionMainFragmentToFragmentCreationTask().setChosenTask(task);
        Navigation.findNavController(view).navigate(action);
    }

}
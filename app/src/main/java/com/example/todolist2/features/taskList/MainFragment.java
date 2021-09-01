package com.example.todolist2.features.taskList;

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

import com.example.todolist2.R;
import com.example.todolist2.data.local.database.entities.Task;
import com.example.todolist2.features.taskList.data.Sort;
import com.example.todolist2.mvp.mvmViews.TasksListView;
import com.example.todolist2.mvp.presenters.TasksListPresenter;

import java.util.List;


public class MainFragment extends Fragment implements TaskListAdapter.TaskListener, TasksListView {
    private Button createNewTaskBtn;
    private Spinner spinnerSort;
    private View view;
    private TaskListAdapter customAdapter;

    //todo старайся удалять код, который не нужен. Студия подсвечивает название методов или полей серым, если они не используются
    private static final String TEXT = "sharedPrefs";
    public static final String POSITION = "position";
    public static final String SWITCH = "switch";

    //todo старайся отказываться от таких мутабельных полей
    private int chosenPosition;
    private Switch hideDoneSwitch;
    private boolean hideDone;
    private List<Task> taskList;


    private TasksListPresenter presenter;

    @Override
    public void showLoading() {
        //todo добавить progerss bar, показвать его здесь
    }

    @Override
    public void hideLoading() {
        //todo добавить progerss bar, скрывать его здесь
    }

    @Override
    public void showTextMessage(String text) {
        //todo toast
    }

    @Override
    public void showList(List<Task> tasks) {

        this.taskList = tasks; //это не нужно
        //todo здесь должен вызываться только customAdapter.setTaskList(tasks)
        //инициализацию recyclerView обычно делают в методе onCreateView
        //в общем, инициализацию вью производят один раз при создании
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        customAdapter = new TaskListAdapter(getContext(), this);
        customAdapter.setTaskList(tasks);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void setSortType(Sort sort, boolean hideDone) {
        //todo переметр sort не нужен
       hideDoneSwitch.setChecked(hideDone);
       presenter.getList();
    }

    @Override
    public void setCompletedTasks(Sort sort, boolean showCompletedTasks) {
        hideDoneSwitch.setChecked(showCompletedTasks);
        //todo презентер сам может вызвать метод changeSort сразу после setCompletedTasks
        presenter.changeSort(sort, showCompletedTasks);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);

        presenter =  new TasksListPresenter(getContext());
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
    public void onTaskClick(Task task) {

        String clickName = task.taskTitle;

        //todo переделать на аргументы
        Bundle result = new Bundle();
        //todo df1 является константой, ее необходимо держать где-то в одном месте,
        // ну и нужно дать более осмысленное название
        result.putParcelable("chosenTaskContent", task);
        getParentFragmentManager().setFragmentResult("chosenTask", result);
        //todo аргументы в android navigation component
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_fragmentCreationTask);

        //todo навигируйся через safeArgs
//        NavDirections action = MainFragmentDirections.actionMainFragmentToFragmentCreationTask();
//        Navigation.findNavController(view).navigate(action);
    }

}
package com.midterm.todolistwidget.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.adapters.TodoAdapter;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TodoRepository repository;
    private RecyclerView recyclerView;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = TodoRepository.getInstance(this);
        setupRecyclerView();
        setupFab();
        loadTasks();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
        });
    }

    private void loadTasks() {
        new AsyncTask<Void, Void, List<Task>>() {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                return repository.getAllTasks();
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        }.execute();
    }

    // Thêm phương thức onResume để refresh danh sách khi quay lại từ AddTaskActivity
    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}
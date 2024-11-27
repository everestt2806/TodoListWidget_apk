package com.midterm.todolistwidget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.adapters.TodoWidgetAdapter;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TodoRepository repository;
    private TodoWidgetAdapter adapter;
    private ListView taskListView;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_main);

            // Initialize Repository
            repository = new TodoRepository(this);

            // Initialize ListView and Adapter
            taskListView = findViewById(R.id.task_list_view);
            taskList = new ArrayList<>();
            adapter = new TodoWidgetAdapter(this, taskList, repository);
            taskListView.setAdapter(adapter);

            // Load Tasks
            loadTasks();

            // Add Task Button
            FloatingActionButton addTaskButton = findViewById(R.id.add_task_button);
            addTaskButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            });

            // Task Click Listener
            taskListView.setOnItemClickListener((parent, view, position, id) -> {
                Task selectedTask = taskList.get(position);
                // Optional: Add edit task functionality
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_LONG).show();
        }
    }

    private void loadTasks() {
        try {
            new Thread(() -> {
                try {
                    List<Task> activeTasks = repository.getActiveTasks();
                    runOnUiThread(() -> {
                        try {
                            if (activeTasks != null) {
                                taskList.clear();
                                taskList.addAll(activeTasks);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "Loaded " + activeTasks.size() + " tasks");
                            } else {
                                Log.w(TAG, "No tasks found");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error updating UI with tasks", e);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error loading tasks", e);
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "Error in loadTasks method", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}
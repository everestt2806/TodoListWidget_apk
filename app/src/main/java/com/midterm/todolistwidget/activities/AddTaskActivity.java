package com.midterm.todolistwidget.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;
import com.midterm.todolistwidget.widgets.TodoWidgetProvider;

public class AddTaskActivity extends AppCompatActivity {
    private EditText taskTitleEditText;
    private EditText taskDescriptionEditText;
    private Button saveTaskButton;
    private TodoRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Initialize views
        taskTitleEditText = findViewById(R.id.task_title_input);
        taskDescriptionEditText = findViewById(R.id.task_description_input);
        saveTaskButton = findViewById(R.id.save_task_button);

        // Initialize Repository
        repository = new TodoRepository(this);

        // Save Task Button Click Listener
        saveTaskButton.setOnClickListener(v -> {
            String title = taskTitleEditText.getText().toString().trim();
            String description = taskDescriptionEditText.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(title)) {
                taskTitleEditText.setError("Task title cannot be empty");
                return;
            }

            // Create new task
            Task newTask = new Task(title, description);

            // Insert task
            new Thread(() -> {
                repository.insert(newTask);

                // Update widgets
                runOnUiThread(() -> {
                    updateWidgets();
                    Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    private void updateWidgets() {
        Intent intent = new Intent(this, TodoWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName componentName = new ComponentName(this, TodoWidgetProvider.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        sendBroadcast(intent);
    }
}
package com.midterm.todolistwidget.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;

public class AddTaskActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button saveButton;
    private Button cancelButton;
    private TodoRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Initialize views
        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        saveButton = findViewById(R.id.buttonSave);
        cancelButton = findViewById(R.id.buttonCancel);

        repository = TodoRepository.getInstance(this);

        saveButton.setOnClickListener(v -> saveTask());

        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveTask() {

        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();


        if (title.isEmpty()) {
            titleEditText.setError("Title cannot be empty");
            titleEditText.requestFocus();
            return;
        }

        Task newTask = new Task(title, description);

        // Save task in background
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                repository.insertTask(newTask);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after saving
            }
        }.execute();
    }
}
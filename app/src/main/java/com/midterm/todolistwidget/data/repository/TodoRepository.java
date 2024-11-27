package com.midterm.todolistwidget.data.repository;

import android.content.Context;
import android.util.Log;

import com.midterm.todolistwidget.data.database.TodoDatabase;
import com.midterm.todolistwidget.data.database.TodoDao;
import com.midterm.todolistwidget.data.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
public class TodoRepository {
    private static final String TAG = "TodoRepository";
    private TodoDao todoDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    public TodoRepository(Context context) {
        try {
            TodoDatabase database = TodoDatabase.getInstance(context);
            todoDao = database.todoDao();
        } catch (Exception e) {
            Log.e(TAG, "Error initializing repository", e);
        }
    }


    public void insert(Task task) {
        try {
            executor.execute(() -> {
                try {
                    long id = todoDao.insertTask(task);
                    Log.d(TAG, "Task inserted with ID: " + id);
                } catch (Exception e) {
                    Log.e(TAG, "Error inserting task", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error in insert method", e);
        }
    }

    public void update(Task task) {
        executor.execute(() -> todoDao.updateTask(task));
    }

    public void delete(Task task) {
        executor.execute(() -> todoDao.deleteTask(task));
    }

    public void clearCompletedTasks() {
        executor.execute(() -> todoDao.clearCompletedTasks());
    }

    public List<Task> getAllTasks() {
        try {
            return todoDao.getAllTasks();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Task> getActiveTasks() {
        try {
            List<Task> tasks = todoDao.getActiveTasks();
            Log.d(TAG, "Retrieved " + (tasks != null ? tasks.size() : 0) + " active tasks");
            return tasks != null ? tasks : new ArrayList<>();
        } catch (Exception e) {
            Log.e(TAG, "Error getting active tasks", e);
            return new ArrayList<>();
        }
    }
}
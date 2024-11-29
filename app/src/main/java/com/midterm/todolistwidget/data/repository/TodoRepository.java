package com.midterm.todolistwidget.data.repository;

import android.content.Context;

import com.midterm.todolistwidget.data.database.TodoDao;
import com.midterm.todolistwidget.data.database.TodoDatabase;
import com.midterm.todolistwidget.data.models.Task;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TodoRepository {
    private final TodoDao todoDao;
    private final Executor executor;

    public TodoRepository(Context context) {
        TodoDatabase db = TodoDatabase.getInstance(context);
        todoDao = db.todoDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public List<Task> getAllTasks() {
        return todoDao.getAllTasks();
    }

    public void insertTask(Task task) {
        executor.execute(() -> todoDao.insertTask(task));
    }

    public void updateTask(Task task) {
        executor.execute(() -> todoDao.updateTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> todoDao.deleteTask(task));
    }

    public void clearCompletedTasks() {
        executor.execute(() -> todoDao.clearCompletedTasks());
    }

    public void updateTaskStatus(long taskId, boolean isCompleted) {
        executor.execute(() -> todoDao.updateTaskStatus(taskId, isCompleted));
    }

    public List<Task> getActiveTasks() {
        return todoDao.getActiveTasks();
    }

    public List<Task> getCompletedTasks() {
        return todoDao.getCompletedTasks();
    }
}

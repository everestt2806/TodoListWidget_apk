package com.midterm.todolistwidget.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.midterm.todolistwidget.data.database.TodoDao;
import com.midterm.todolistwidget.data.database.TodoDatabase;
import com.midterm.todolistwidget.data.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TodoRepository {
    private TodoDao todoDao;
    private static TodoRepository instance;

    private TodoRepository(Context context) {
        TodoDatabase database = TodoDatabase.getInstance(context);
        todoDao = database.todoDao();
    }

    public static synchronized TodoRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TodoRepository(context);
        }
        return instance;
    }

    public void insertTask(Task task) {
        new InsertTaskAsyncTask(todoDao).execute(task);
    }

    public void updateTask(Task task) {
        new UpdateTaskAsyncTask(todoDao).execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTaskAsyncTask(todoDao).execute(task);
    }

    public List<Task> getAllTasks() {
        try {
            return new GetAllTasksAsyncTask(todoDao).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //add
    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TodoDao todoDao;

        private InsertTaskAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            todoDao.insertTask(tasks[0]);
            return null;
        }
    }

    //update
    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TodoDao todoDao;

        private UpdateTaskAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            todoDao.updateTask(tasks[0]);
            return null;
        }
    }

    //delete
    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TodoDao todoDao;

        private DeleteTaskAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            todoDao.deleteTask(tasks[0]);
            return null;
        }
    }

    //get all tasks
    private static class GetAllTasksAsyncTask extends AsyncTask<Void, Void, List<Task>> {
        private TodoDao todoDao;

        private GetAllTasksAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {
            return todoDao.getAllTasks();
        }
    }
}
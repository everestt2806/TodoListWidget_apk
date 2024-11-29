package com.midterm.todolistwidget.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.midterm.todolistwidget.data.models.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    private static final String TAG = "TodoDatabase";
    private static TodoDatabase instance;

    public abstract TodoDao todoDao();

    public static synchronized TodoDatabase getInstance(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null, cannot create database");
            throw new IllegalArgumentException("Context cannot be null");
        }

        if (instance == null) {
            try {
                instance = Room.databaseBuilder(
                                context.getApplicationContext(),
                                TodoDatabase.class,
                                "todo_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build();
                Log.d(TAG, "Database instance created");
            } catch (Exception e) {
                Log.e(TAG, "Error creating database instance", e);
                throw new RuntimeException("Could not create database", e);
            }
        }
        return instance;
    }
}

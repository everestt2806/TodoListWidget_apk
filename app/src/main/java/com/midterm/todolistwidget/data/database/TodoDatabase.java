package com.midterm.todolistwidget.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.midterm.todolistwidget.data.models.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase instance;
    public abstract TodoDao todoDao();

    public static synchronized TodoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TodoDatabase.class,
                    "todo_database"
            ).build();
        }
        return instance;
    }
}

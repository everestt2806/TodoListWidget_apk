package com.midterm.todolistwidget.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.midterm.todolistwidget.data.models.Task;

import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    List<Task> getAllTasks();

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    Task getTaskById(int taskId);
}


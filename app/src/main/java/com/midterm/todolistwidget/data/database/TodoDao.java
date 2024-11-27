package com.midterm.todolistwidget.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.midterm.todolistwidget.data.models.Task;
import java.util.List;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM tasks ORDER BY timestamp DESC")
    List<Task> getAllTasks();
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY timestamp DESC")
    List<Task> getActiveTasks();

    @Insert
    long insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    void clearCompletedTasks();
}
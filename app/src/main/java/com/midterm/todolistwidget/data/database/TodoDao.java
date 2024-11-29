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
    @Query("SELECT * FROM tasks ORDER BY created_at DESC")
    List<Task> getAllTasks();

    @Query("UPDATE tasks SET completed = :isCompleted WHERE id = :taskId")
    void updateTaskStatus(long taskId, boolean isCompleted);

    @Insert
    long insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM tasks WHERE completed = 1")
    void clearCompletedTasks();

    @Query("SELECT * FROM tasks WHERE completed = 0")
    List<Task> getActiveTasks();

    @Query("SELECT * FROM tasks WHERE completed = 1")
    List<Task> getCompletedTasks();
}

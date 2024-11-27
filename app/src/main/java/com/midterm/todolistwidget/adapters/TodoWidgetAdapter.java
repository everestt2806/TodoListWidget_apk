package com.midterm.todolistwidget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;

import java.util.List;

public class TodoWidgetAdapter extends ArrayAdapter<Task> {
    private Context context;
    private List<Task> tasks;
    private TodoRepository repository;

    public TodoWidgetAdapter(@NonNull Context context, List<Task> tasks, TodoRepository repository) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        this.repository = repository;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        }

        Task currentTask = tasks.get(position);

        TextView titleTextView = listItem.findViewById(R.id.task_title);
        CheckBox completeCheckBox = listItem.findViewById(R.id.task_complete_checkbox);

        titleTextView.setText(currentTask.getTitle());
        completeCheckBox.setChecked(currentTask.isCompleted());

        completeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTask.setCompleted(isChecked);
            repository.update(currentTask);
        });

        return listItem;
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks.clear();
        this.tasks.addAll(newTasks);
        notifyDataSetChanged();
    }
}
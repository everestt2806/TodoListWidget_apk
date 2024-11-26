package com.midterm.todolistwidget.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoWidgetItemFactory(getApplicationContext());
    }
}

class TodoWidgetItemFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Task> tasks;
    private TodoRepository repository;

    TodoWidgetItemFactory(Context context) {
        this.context = context;
        this.repository = TodoRepository.getInstance(context);
    }

    @Override
    public void onCreate() {
        // Initialize the data
        tasks = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        // Refresh the task list
        tasks = repository.getAllTasks();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= tasks.size()) return null;

        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_item
        );

        Task task = tasks.get(position);

        // Set task title
        views.setTextViewText(R.id.task_title, task.getTitle());

        // Set task completion status using ImageView
        int imageRes = task.isCompleted() ? R.drawable.ic_task_completed : R.drawable.ic_task_incomplete;
        views.setImageViewResource(R.id.completed_image, imageRes);

        // Set up click intent
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("task_id", task.getId());
        views.setOnClickFillInIntent(R.id.task_title, fillInIntent);

        return views;
    }



    @Override
    public int getCount() {
        return tasks.size();
    }

    // Other required methods...
    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(String.valueOf(getLoadingView()), R.layout.loading_widget);
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        tasks.clear();
    }
}


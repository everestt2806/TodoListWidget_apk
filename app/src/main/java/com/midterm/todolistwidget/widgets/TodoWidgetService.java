package com.midterm.todolistwidget.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.data.models.Task;
import com.midterm.todolistwidget.data.repository.TodoRepository;

import java.util.List;

public class TodoWidgetService extends RemoteViewsService {
    private static final int INVALID_POSITION = -1;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoWidgetItemFactory(getApplicationContext());
    }

    private class TodoWidgetItemFactory implements RemoteViewsFactory {
        private final Context context;
        private final TodoRepository repository;
        private List<Task> tasks;

        public TodoWidgetItemFactory(Context context) {
            this.context = context;
            this.repository = new TodoRepository(context);
        }

        @Override
        public void onCreate() {
            loadTasks();
        }

        @Override
        public void onDataSetChanged() {
            loadTasks();
        }

        private void loadTasks() {
            try {
                tasks = repository.getActiveTasks();
            } catch (Exception e) {
                e.printStackTrace();
                tasks = null;
            }
        }

        @Override
        public int getCount() {
            return tasks != null ? tasks.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == INVALID_POSITION || tasks == null || position >= tasks.size()) {
                return null;
            }

            Task task = tasks.get(position);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            try {
                // Set task title
                views.setTextViewText(R.id.widget_task_title, task.getTitle());

                // Set checkbox state
                views.setImageViewResource(R.id.checkbox_todo,
                        task.isCompleted()
                                ? R.drawable.ic_checkbox_checked
                                : R.drawable.ic_checkbox_unchecked);

                // Create fill-in intent for item click
                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(TodoWidgetProvider.EXTRA_ITEM_POSITION, position);
                fillInIntent.putExtra(TodoWidgetProvider.EXTRA_ITEM_ID, task.getId());

                // Set click listeners for both the title and checkbox
                views.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);
                views.setOnClickFillInIntent(R.id.checkbox_todo, fillInIntent);
                views.setOnClickFillInIntent(R.id.widget_task_title, fillInIntent);

                return views;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(context.getPackageName(), R.layout.widget_loading);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return tasks != null && position < tasks.size() ? tasks.get(position).getId() : position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDestroy() {
            tasks = null;
        }
    }
}

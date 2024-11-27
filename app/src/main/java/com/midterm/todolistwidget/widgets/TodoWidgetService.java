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

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoWidgetItemFactory(getApplicationContext());
    }

    private class TodoWidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private TodoRepository repository;
        private List<Task> tasks;

        public TodoWidgetItemFactory(Context context) {
            this.context = context;
            this.repository = new TodoRepository(context);
        }

        @Override
        public void onCreate() {
            // Initial data load
            tasks = repository.getActiveTasks();
        }

        @Override
        public void onDataSetChanged() {
            // Refresh data when widget is updated
            tasks = repository.getActiveTasks();
        }

        @Override
        public int getCount() {
            return tasks != null ? tasks.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (tasks == null || tasks.isEmpty()) {
                // Trả về view mặc định khi không có task nào
                RemoteViews emptyView = new RemoteViews(context.getPackageName(), R.layout.widget_empty);
                return emptyView;
            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            Task task = tasks.get(position);
            views.setTextViewText(R.id.widget_task_title, task.getTitle());

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            // Trả về view "loading" nếu cần thiết
            RemoteViews loadingView = new RemoteViews(context.getPackageName(), R.layout.widget_loading);
            return loadingView;
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
            // Clean up resources if needed
        }
    }
}

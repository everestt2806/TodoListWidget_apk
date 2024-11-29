package com.midterm.todolistwidget.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.activities.MainActivity;

public class TodoWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_TOGGLE_TASK = "com.midterm.todolistwidget.ACTION_TOGGLE_TASK";
    public static final String EXTRA_ITEM_POSITION = "com.midterm.todolistwidget.EXTRA_ITEM_POSITION";
    public static final String EXTRA_ITEM_ID = "com.midterm.todolistwidget.EXTRA_ITEM_ID";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Intent to launch main activity when widget header is clicked
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widget_container, mainPendingIntent);

            // Set up the list view with a remote adapter
            Intent serviceIntent = new Intent(context, TodoWidgetService.class);
            views.setRemoteAdapter(R.id.widget_list, serviceIntent);

            // Template to handle item clicks
            Intent clickIntent = new Intent(context, TodoWidgetProvider.class);
            clickIntent.setAction(ACTION_TOGGLE_TASK);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0,
                    clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

            // Set the pending intent template
            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntent);

            // Empty view for when the list has no items
            views.setEmptyView(R.id.widget_list, R.id.widget_empty_text);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_TOGGLE_TASK.equals(intent.getAction())) {
            int position = intent.getIntExtra(EXTRA_ITEM_POSITION, -1);
            long taskId = intent.getLongExtra(EXTRA_ITEM_ID, -1);

            if (position != -1 && taskId != -1) {
                // Toggle task completion status
                toggleTaskCompletion(context, taskId);

                // Update widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName componentName = new ComponentName(context, TodoWidgetProvider.class);
                appWidgetManager.notifyAppWidgetViewDataChanged(
                        appWidgetManager.getAppWidgetIds(componentName),
                        R.id.widget_list
                );
            }
        }
    }

    private void toggleTaskCompletion(Context context, long taskId) {
        // Implement your database logic here to toggle task completion
        // Example:
        // TaskDatabase db = TaskDatabase.getInstance(context);
        // db.taskDao().toggleTaskCompletion(taskId);
    }
}

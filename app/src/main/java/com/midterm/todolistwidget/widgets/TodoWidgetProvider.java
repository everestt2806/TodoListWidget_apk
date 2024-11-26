package com.midterm.todolistwidget.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.midterm.todolistwidget.R;
import com.midterm.todolistwidget.activities.AddTaskActivity;

public class TodoWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        // Set up the intent for the ListView
        Intent serviceIntent = new Intent(context, TodoWidgetService.class);
        views.setRemoteAdapter(R.id.widget_listview, serviceIntent);

        // Set up the intent for the "Add" button
        Intent addIntent = new Intent(context, AddTaskActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                addIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        views.setOnClickPendingIntent(R.id.add_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

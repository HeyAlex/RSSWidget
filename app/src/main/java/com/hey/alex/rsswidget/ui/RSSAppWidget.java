package com.hey.alex.rsswidget.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hey.alex.rsswidget.R;

/**
 * Implementation of App Widget functionality.
 */
public class RSSAppWidget extends AppWidgetProvider {

    public static final String TAG = "RSSAppWidget";

    public static final String RSS_UPDATE_COMPLETE = "RSS_UPDATE_ON_TASK_COMPLETE";

    public static final String RSS_CLICK_RIGHT = "RSS_CLICK_RIGHT";
    public static final String RSS_CLICK_LEFT = "RSS_CLICK_LEFT";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rssapp_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case RSS_UPDATE_COMPLETE:

            case RSS_CLICK_RIGHT:

            case RSS_CLICK_LEFT:
                default:
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


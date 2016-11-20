package com.hey.alex.rsswidget.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.hey.alex.rsswidget.R;
import com.hey.alex.rsswidget.model.Channel;

import static com.hey.alex.rsswidget.service.RssService.setupAlarm;

/**
 * Implementation of App Widget functionality.
 */
public class RSSAppWidget extends AppWidgetProvider {

    public static final String TAG = "RSSAppWidget";

    public static final String RSS_UPDATE_COMPLETE = "RSS_UPDATE_ON_TASK_COMPLETE";

    public static final String RSS_CLICK_RIGHT = "RSS_CLICK_RIGHT";
    public static final String RSS_CLICK_LEFT = "RSS_CLICK_LEFT";

    public static final String RSS_PREF = "RSS_PREF";
    private static Channel channel;
    private static int sizeRss;
    private static int currentItem = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rssapp_widget);
        if(channel != null){
            //sizeRss = channel.getRssItems().size() -1;
            Log.d(TAG, "channel not null");
            views.setTextViewText(R.id.title, channel.getRssItems().get(currentItem).getTitle());
            views.setTextViewText(R.id.description, channel.getRssItems().get(currentItem).getDescription());


            Intent rightButtonIntent = new Intent(context, RSSAppWidget.class);
            rightButtonIntent.setAction(RSS_CLICK_RIGHT);
            PendingIntent rightButtonPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, rightButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.right_button,rightButtonPendingIntent);

            Intent leftButtonIntent = new Intent(context, RSSAppWidget.class);
            leftButtonIntent.setAction(RSS_CLICK_LEFT);
            PendingIntent leftButtonPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, leftButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.left_button,leftButtonPendingIntent);

        }
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
                Log.d(TAG, "RSS_UPDATE_COMPLETE");

                channel = intent.getParcelableExtra("channel");
                invokeUpdate(context);
                break;
            case RSS_CLICK_RIGHT:
                Log.d(TAG, "right");
                currentItem++;
                if(currentItem == channel.getRssItems().size() - 1) currentItem = 0;
                invokeUpdate(context);
                break;
            case RSS_CLICK_LEFT:
                Log.d(TAG, "left");
                currentItem--;
                if(currentItem == -1) currentItem = channel.getRssItems().size() - 1;
                invokeUpdate(context);
                break;
            default:
        }

        super.onReceive(context, intent);
    }

    private void invokeUpdate(Context context) {
        final int[] ids = getWidgetsIds(context);
        onUpdate(context, AppWidgetManager.getInstance(context), ids);
    }

    private int[] getWidgetsIds(Context context) {
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final ComponentName provider = new ComponentName(context.getPackageName(), getClass().getName());
        return manager.getAppWidgetIds(provider);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        setupAlarm(context);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }
}


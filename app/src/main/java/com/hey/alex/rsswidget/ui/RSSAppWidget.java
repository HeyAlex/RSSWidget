package com.hey.alex.rsswidget.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hey.alex.rsswidget.R;
import com.hey.alex.rsswidget.model.Channel;

import static com.hey.alex.rsswidget.ui.SettingWidgetActivity.setupAlarm;
import static com.hey.alex.rsswidget.ui.SettingWidgetActivity.stopAlarm;

//import static com.hey.alex.rsswidget.service.RssService.setupAlarm;

/**
 * Implementation of App Widget functionality.
 */
public class RSSAppWidget extends AppWidgetProvider {

    public static final String TAG = "RSSAppWidget";
    public static final String UpdateCheckerLog = "UpdateCheckerLog";

    public static final String RSS_UPDATE_COMPLETE = "RSS_UPDATE_ON_TASK_COMPLETE";
    public static final String RSS_CLICK_RIGHT = "RSS_CLICK_RIGHT";
    public static final String RSS_CLICK_LEFT = "RSS_CLICK_LEFT";

    private static Channel channel;
    private static String title;
    private static int currentItem = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rssapp_widget);
        if(channel != null && channel.getRssItems().size() != 0){

            views.setTextViewText(R.id.title, channel.getRssItems().get(currentItem).getTitle());
            views.setTextViewText(R.id.description, channel.getRssItems().get(currentItem).getDescription());
            views.setTextViewText(R.id.channelName, title);

            if(currentItem == channel.getRssItems().size() - 1) {
                views.setBoolean(R.id.right_button,"setEnabled",false);
                views.setViewVisibility(R.id.right_button, View.INVISIBLE);
            }
            else {
                views.setBoolean(R.id.right_button,"setEnabled",true);
                views.setViewVisibility(R.id.right_button, View.VISIBLE);
            }
            Intent rightButtonIntent = new Intent(context, RSSAppWidget.class);
            rightButtonIntent.setAction(RSS_CLICK_RIGHT);
            PendingIntent rightButtonPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, rightButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.right_button,rightButtonPendingIntent);



            if (currentItem == 0) {
                views.setBoolean(R.id.left_button,"setEnabled",false);
                views.setViewVisibility(R.id.left_button, View.INVISIBLE);
            }
            else {
                views.setBoolean(R.id.left_button,"setEnabled",true);
                views.setViewVisibility(R.id.left_button, View.VISIBLE);
            }
            Intent leftButtonIntent = new Intent(context, RSSAppWidget.class);
            leftButtonIntent.setAction(RSS_CLICK_LEFT);
            PendingIntent leftButtonPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, leftButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.left_button,leftButtonPendingIntent);

        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case RSS_UPDATE_COMPLETE:
                Log.d(UpdateCheckerLog, "UpdateCheckerLog");
                try{
                    title = intent.getStringExtra("title");
                    channel = intent.getParcelableExtra("channel");
                }catch (NullPointerException ex){
                    Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                }

                break;

            case RSS_CLICK_RIGHT:
                Log.d(TAG, "right");
                currentItem++;
                break;

            case RSS_CLICK_LEFT:
                Log.d(TAG, "left");
                currentItem--;
                break;
            default:
        }
        invokeUpdate(context); 
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
        super.onEnabled(context);
        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        stopAlarm(context);
        Log.d(TAG, "onDisabled");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        setupAlarm(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}


package com.hey.alex.rsswidget.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hey.alex.rsswidget.model.Channel;
import com.hey.alex.rsswidget.ui.RSSAppWidget;
import com.hey.alex.rsswidget.util.UtilClass;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by alexf on 19.11.2016.
 */

public class RssService extends IntentService{

    public static final String TAG = "RssService";

    public static final String RSS_UPDATE_COMPLETE = "RSS_UPDATE_ON_TASK_COMPLETE";
    public static final String RSS_UPDATE = "RSS_UPDATE";
  //  public static final String RSS_INVOKE = "RSS_INVOKE";



    public RssService() {
        super(TAG);
        Log.d(TAG, "INTENT SERVICE CONSTRUCTOR");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        final String rssUrl = "https://miet.ru/rss/news";
        Channel channel = null;
        try {
            Log.d(TAG, "getting channel");
            channel = UtilClass.getChannel(rssUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        sendBroadcast(getRSScompleteIntent(channel));
        Log.d(TAG, "sendBroadcast");
//        if(intent != null){
//            if(intent.getAction().equals(RSS_UPDATE)){
//                Log.d(TAG,"RSS_UPDATE event");
//                final String rssUrl = "";
//                final int id = 1;
//
//                try {
//                    Channel channel = UtilClass.getChannel(rssUrl);
//                    sendBroadcast(getRSScompleteIntent(channel,id));
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        //HANDLE ACTION

        //LOAD RSS AND SEND BROADCAST THAT LOADING COMPLETE
    }


    private Intent getRSScompleteIntent(Channel channel){
        Intent intent = new Intent(getBaseContext(), RSSAppWidget.class);
        intent.setAction(RSS_UPDATE_COMPLETE);
        intent.putExtra("channel", channel);
       // intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        return intent;
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, RssService.class);
        intent.setAction(RSS_UPDATE);
        // intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void setupAlarm(Context context) {
        Log.d(TAG, "setupAlarm");
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60*1000, pendingIntent);
    }




    //SET ALARM THAT WILL SEND PENDING INTENT FOR RSS SERVICE EVERY MINUTE AND WATCH INTENT IN ONHANDLEINTENT() METHOD
    //SO AFTER DOWNLOADING SEND BROADCAST THAT LOADING COMPLETE TO WIDGET WITH CURRENT ID
}

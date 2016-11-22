package com.hey.alex.rsswidget.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import static com.hey.alex.rsswidget.ui.SettingWidgetActivity.setupAlarm;

/**
 * Created by alexf on 22.11.2016.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static final String RSS_UPDATE = "RSS_UPDATE";
    public static final String TAG = "ALARM_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, TAG);
        Intent service = new Intent(context, RssService.class);
        service.setAction(RSS_UPDATE);
        startWakefulService(context, service);
        Log.d(TAG, "start service");
      //  setupAlarm(context);
    }
}

package com.hey.alex.rsswidget.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.hey.alex.rsswidget.ui.SettingWidgetActivity.setupAlarm;

/**
 * Created by alexf on 22.11.2016.
 */

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = "BootREceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            setupAlarm(context);
        }
    }
}

package com.hey.alex.rsswidget.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hey.alex.rsswidget.R;
import com.hey.alex.rsswidget.service.RssService;
import com.hey.alex.rsswidget.util.PrefUtils;
import com.hey.alex.rsswidget.util.UtilClass;

/**
 * Created by alexf on 20.11.2016.
 */

public class SettingWidgetActivity extends AppCompatActivity {

    public static final String TAG = "SettingWidgetActivity";
    public static final String RSS_UPDATE = "RSS_UPDATE";
    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        setupAlarm(this);
        final EditText rssUrl = (EditText) findViewById(R.id.editTextUrl);
        final TextView error = (TextView) findViewById(R.id.error);
        final Button btnEnter = (Button) findViewById(R.id.enter);
        rssUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (UtilClass.urlValidation(rssUrl.getText().toString())) {
                    btnEnter.setEnabled(true);
                    error.setText("");
                } else {
                    if (UtilClass.mainValidation(rssUrl.getText().toString()))
                        error.setText("URL should be correct");
                    else error.setText("Start URL with \"http://\" ");
                    btnEnter.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rssUrltext = rssUrl.getText().toString();
                PrefUtils.saveToPrefs(getBaseContext(), "rssUrl", rssUrltext);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

                setResult(RESULT_OK, resultValue);

                Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, SettingWidgetActivity.this, RSSAppWidget.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
                sendBroadcast(intent);

                finish();
            }
        });
    }


    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, RssService.class);
        intent.setAction(RSS_UPDATE);
        //  intent.putExtra("url",);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void setupAlarm(Context context) {
        Log.d(TAG, "setupAlarm");
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60 * 1000, pendingIntent);

    }
}

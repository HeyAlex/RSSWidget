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
import com.hey.alex.rsswidget.util.PrefUtils;
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
      //  final String rssUrl = "https://lenta.ru/rss/news";//"https://miet.ru/rss/news";
      //  Channel channel = null;
      /*  try {
            Log.d(TAG, "getting channel");
            channel = UtilClass.getChannel(rssUrl);
            Log.d(TAG, channel.getTitle());
            sendBroadcast(getRSScompleteIntent(channel));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
*/

        if(intent != null){
            if(intent.getAction().equals(RSS_UPDATE)){
                Log.d(TAG,"RSS_UPDATE event");

              //  final String rssUrl = intent.getStringExtra("url");

                try {
                    Channel channel = UtilClass.getChannel(PrefUtils.getFromPrefs(this,"rssUrl",""));
                    sendBroadcast(getRSScompleteIntent(channel));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }
        //HANDLE ACTION

        //LOAD RSS AND SEND BROADCAST THAT LOADING COMPLETE
    }


    private Intent getRSScompleteIntent(Channel channel){
        Log.d(TAG, channel.getTitle());
        Intent intent = new Intent(getBaseContext(), RSSAppWidget.class);
        intent.setAction(RSS_UPDATE_COMPLETE);
        intent.putExtra("title",channel.getTitle());
        intent.putExtra("channel", channel);
        return intent;
    }


}




    //SET ALARM THAT WILL SEND PENDING INTENT FOR RSS SERVICE EVERY MINUTE AND WATCH INTENT IN ONHANDLEINTENT() METHOD
    //SO AFTER DOWNLOADING SEND BROADCAST THAT LOADING COMPLETE TO WIDGET WITH CURRENT ID


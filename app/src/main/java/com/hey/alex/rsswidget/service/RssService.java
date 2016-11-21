package com.hey.alex.rsswidget.service;

import android.app.IntentService;
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
        if(intent != null){
            if(intent.getAction().equals(RSS_UPDATE)){
                Log.d(TAG,"RSS_UPDATE event");
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
    }


    private Intent getRSScompleteIntent(Channel channel){

        Intent intent = new Intent(getBaseContext(), RSSAppWidget.class);
        intent.setAction(RSS_UPDATE_COMPLETE);
        intent.putExtra("channel", channel);
        if(channel.getTitle() != null){
            intent.putExtra("title",channel.getTitle());
        }
        return intent;
    }

}



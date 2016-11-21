package com.hey.alex.rsswidget.util;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.Xml;

import com.hey.alex.rsswidget.model.Channel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ALEXEY on 10/19/2016.
 */

public class UtilClass {

    public static final String TAG = "UtilClass";


    public static Channel getChannel(String current_url) throws IOException, XmlPullParserException {
        Log.d(TAG,"CHANNEL DOWNLOADED");
        Channel channel = new Channel();
        URL url = new URL(current_url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setUseCaches(true);
        conn.connect();

        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStream is = conn.getInputStream();
            BufferedInputStream reader = new BufferedInputStream(is);
            channel = parse(reader);
            channel.setUrl(current_url);
            Log.d(TAG,"CHANNEL DOWNLOADED");
            is.close();
        }

        return channel;
    }

    private static String readFromStream(InputStream is) throws IOException {
        String line;
        StringBuilder total = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while ((line = reader.readLine()) != null) {
            total.append(line);
        }

        line = total.toString();
        return line;
    }


    //VALIDATION
    public static boolean mainValidation(String url) {
        return url.contains("http://");
    }

    public static boolean urlValidation(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }


    //RSS PARSING
    private static Channel parse(InputStream in) throws XmlPullParserException, IOException {
        Channel channel = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        int eventType = parser.getEventType();
        Channel.RssItem currentItem = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = "";
            String namespace = "";
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    Log.d(TAG,"let's parse");
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    namespace = parser.getNamespace();
                    if (name.equalsIgnoreCase("channel")) {
                        channel = new Channel();
                    } else if (!TextUtils.isEmpty(namespace) || name.equalsIgnoreCase("image")) {
                        break;
                    } else if (name.equalsIgnoreCase("item")) {
                        currentItem = new Channel.RssItem();
                    } else if (currentItem != null) {
                        itemNode(parser, currentItem, name);
                    } else if (channel != null) {
                        channelNode(parser, channel, name);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (channel != null && name.equals("item")) {
                        channel.addRssItem(currentItem);
                    } else if (name.equalsIgnoreCase("channel")) {
                        Log.d(TAG,"done parsing");
                    }
                    break;
            }
            eventType = parser.next();
        }
        Log.d(TAG, String.valueOf(channel.getRssItems().size()));
        return channel;
    }

    private static void itemNode(XmlPullParser parser, Channel.RssItem item, String name) throws IOException, XmlPullParserException {
        switch (name) {
            case "title":
                item.setTitle(Html.fromHtml(parser.nextText()).toString());
                break;

            case "description":
                item.setDescription(Html.fromHtml(parser.nextText()).toString());
                break;

            default:
                break;
        }
    }

    private static void channelNode(XmlPullParser parser, Channel channel, String name) throws IOException, XmlPullParserException {
        switch (name) {
            case "title":
                channel.setTitle(parser.nextText());
                Log.d(TAG, channel.getTitle());
                break;

            case "description":
                channel.setDescription(parser.nextText());
                break;

            default:
                break;
        }
    }

}

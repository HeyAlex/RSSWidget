package com.hey.alex.rsswidget.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alexf on 19.11.2016.
 */

//RSS CHANNEL
public class Channel implements Parcelable{

    private String url;

    private String title;

    private String description;

    private List<RssItem> rssItems;

    public Channel(Parcel in) {
        url = in.readString();
        title = in.readString();
        description = in.readString();
        rssItems = in.createTypedArrayList(RssItem.CREATOR);
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<RssItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(List<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    public Channel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeTypedList(rssItems);
    }

    public void addRssItem(Channel.RssItem item){
        rssItems.add(item);
    }


    //RSS ITEM
    public static class RssItem implements Parcelable {

        private String title;
        private String description;

        public RssItem(Parcel in) {
            title = in.readString();
            description = in.readString();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
            @Override
            public RssItem createFromParcel(Parcel in) {
                return new RssItem(in);
            }

            @Override
            public RssItem[] newArray(int size) {
                return new RssItem[size];
            }
        };

        public RssItem() {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeString(description);
        }
    }
}

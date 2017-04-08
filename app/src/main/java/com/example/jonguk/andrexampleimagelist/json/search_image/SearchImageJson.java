package com.example.jonguk.andrexampleimagelist.json.search_image;

import android.util.Log;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class SearchImageJson {
    private static final String TAG = "SearchImageJson";

    public String pubDate;
    public String title;
    public String thumbnail;
    public String image;
    public String link;
    public String height;
    public String width;
    public String cp;
    public String cpname;

    @Override
    public boolean equals(Object obj) {
        try {
            SearchImageJson json = (SearchImageJson) obj;
            return pubDate.equals(json.pubDate) && title.equals(json.title)
                    && image.equals(json.image) && link.equals(json.link);

        } catch (Exception e) {
            Log.w(TAG, "equals", e);
            return false;
        }
    }

    @Override
    public String toString() {
        return "SearchImageJson{" +
                "pubDate='" + pubDate + '\'' +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                ", cp='" + cp + '\'' +
                ", cpname='" + cpname + '\'' +
                '}';
    }
}

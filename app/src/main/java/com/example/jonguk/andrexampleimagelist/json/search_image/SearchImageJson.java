package com.example.jonguk.andrexampleimagelist.json.search_image;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class SearchImageJson {
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

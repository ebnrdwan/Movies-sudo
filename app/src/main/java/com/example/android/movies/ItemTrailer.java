package com.example.android.movies;

/**
 * Created by Abdulrhman on 16/11/2016.
 */
public class ItemTrailer {
    private String title ;
    private String key ;

    public ItemTrailer(String title, String key) {
        this.title = title;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

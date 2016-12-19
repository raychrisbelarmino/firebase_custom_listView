package com.example.chiz.list_view_adapter;

/**
 * Created by chiz on 12/18/16.
 */

public class Recipes {
    private String title;
    private String url;
    private String image_url;

    public Recipes() {
    }

    public Recipes(String title, String url, String image_url){
        super();
        this.title = title;
        this.url = url;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage(String image_url) {
        image_url = image_url;
    }
}

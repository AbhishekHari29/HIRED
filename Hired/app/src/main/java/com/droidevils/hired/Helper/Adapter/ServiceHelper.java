package com.droidevils.hired.Helper.Adapter;

public class ServiceHelper {

    private int image;
    private String id, title, desc;
    private float rating;

    public ServiceHelper(int image, String id, String title, String desc, float rating) {
        this.image = image;
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public float getRating() {
        return rating;
    }
}

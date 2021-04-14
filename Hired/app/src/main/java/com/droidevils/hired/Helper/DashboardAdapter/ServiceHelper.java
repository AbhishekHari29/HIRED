package com.droidevils.hired.Helper.DashboardAdapter;

public class ServiceHelper {

    private int image;
    private float rating;
    private String title, desc;

    public ServiceHelper(int image, float rating, String title, String desc) {
        this.image = image;
        this.rating = rating;
        this.title = title;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}

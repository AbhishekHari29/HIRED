package com.droidevils.hired.Helper.DashboardAdapter;

public class CategoryHelper {

    private int image;
    private String title;

    public CategoryHelper(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}

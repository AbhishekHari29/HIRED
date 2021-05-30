package com.droidevils.hired.Helper.Adapter;

import android.graphics.drawable.Drawable;

public class CategoryHelper {

    private int image;
    private String id;
    private String title;
    private Drawable background;

    public CategoryHelper(int image, String id, String title, Drawable background) {
        this.image = image;
        this.id = id;
        this.title = title;
        this.background = background;
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

    public Drawable getBackground() {
        return background;
    }
}

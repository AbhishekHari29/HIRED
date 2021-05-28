package com.droidevils.hired.Helper.Adapter;

import android.graphics.drawable.Drawable;

public class CategoryHelper {

    private int image;
    private String title;
    private Drawable background;

    public CategoryHelper(int image, String title, Drawable background) {
        this.image = image;
        this.title = title;
        this.background = background;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getBackground(){ return background; }
}

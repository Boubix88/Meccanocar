package com.example.meccanocar.model;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private String description;
    private String ref;
    private Bitmap bmp;

    public Item(String n, String d, String r, Bitmap bmp){
        this.name = n;
        this.description = d;
        this.ref = r;
        this.bmp = bmp;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRef() {
        return ref;
    }

    public Bitmap getBitmap() {
        return bmp;
    }
}

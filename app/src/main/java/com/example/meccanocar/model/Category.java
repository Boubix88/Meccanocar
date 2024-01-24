package com.example.meccanocar.model;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private ArrayList<SubCategory> category;
    private String name;
    private String urlImage;
    private int id;

    public Category(String n, String url, int id) {
        this.category = new ArrayList<>();
        this.name = n;
        this.urlImage = url;
        this.id = id;
    }

    public void addSubCategory(SubCategory subCategory) {
        this.category.add(subCategory);
    }

    public String getName() {
        return name;
    }

    public ArrayList<SubCategory> getSubCategorys() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toStringItems(){
        StringBuilder builder = new StringBuilder();
        for(SubCategory i : category){
            builder.append(i.getName() + "\n");
        }
        return builder.toString();
    }

    public String getUrlImage() {
        return urlImage;
    }

    public int getId() {
        return id;
    }

    public void loadImage(ImageView image) {
        Picasso.get().load(this.urlImage).into(image);
    }
}

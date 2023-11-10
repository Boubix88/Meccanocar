package com.example.meccanocar.model;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class Category {
    private ArrayList<Item> category;
    private String name;
    private String urlImage;

    public Category(String n, String url) {
        this.category = new ArrayList<>();
        this.name = n;
        this.urlImage = url;
    }

    public void addItem(Item item) {
        this.category.add(item);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getItems() {
        return category;
    }
}

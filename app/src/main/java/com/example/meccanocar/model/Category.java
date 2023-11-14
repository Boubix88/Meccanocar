package com.example.meccanocar.model;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private ArrayList<Item> category;
    private String name;
    private String urlImage;
    private int id;

    public Category(String n, String url, int id) {
        this.category = new ArrayList<>();
        this.name = n;
        this.urlImage = url;
        this.id = id;
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

    // Ajoutez cette méthode pour afficher correctement le titre dans le Spinner
    @Override
    public String toString() {
        return name;
    }

    public String toStringItems(){
        StringBuilder builder = new StringBuilder();
        for(Item i : category){
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
}

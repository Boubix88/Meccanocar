package com.example.meccanocar.model;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String link;
    private int id;

    public Item(String n, String l, int id){
        this.name = n;
        this.link = l;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getCategoryId(){
        return id;
    }
}

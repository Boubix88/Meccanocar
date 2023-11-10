package com.example.meccanocar.model;

public class Meccanocar {
    private static String URL = "https://www.meccanocar.fr/";
    private Catalog catalog;

    public Meccanocar(){
        this.catalog = new Catalog(URL);
    }

    public Catalog getCatalog(){
        return this.catalog;
    }
}

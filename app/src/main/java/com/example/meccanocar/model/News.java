package com.example.meccanocar.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class News implements Serializable {
    private String imageUrl;
    private String date;
    private String title;
    private String recap;
    private String[] description;
    private String url;
    private String newsUrl = "/fr/news";

    public News(String title, String date, String imageUrl, String recap, String[] description) {
        this.title = title;
        this.date = date;
        this.imageUrl = imageUrl;
        this.recap = recap;
        this.description = description;
    }

    public News(String url) {
        this.title = "";
        this.description = null;
        this.date = "";
        this.imageUrl = "";
        this.recap = "";
        this.url = url;

        // On récupère les news depuis le site
        //getNewsFromHttp();
    }

    public String getTitle() {
        return title;
    }

    public String[] getDescription() {
        return description;
    }

    /*public String getFullDescription() {
        String fullDescription = "";
        for (String s : description) {
            fullDescription += s + "\n";
        }
        return fullDescription;
    }*/

    public String getFullDescription() {
        String htmlDescription = "";
        // Supprime les balises <p> et </p>
        htmlDescription = description[0].replaceAll("<p>", "");
        htmlDescription = htmlDescription.replaceAll("</p>", "");

        // Remplace les balises <br> par des sauts de ligne \n
        htmlDescription = htmlDescription.replaceAll("<br>", "\n");

        // Supprime les null
        htmlDescription = htmlDescription.replaceAll("null", "\n");

        // Supprime les balises <b> et </b>
        htmlDescription = htmlDescription.replaceAll("<b>", "");
        htmlDescription = htmlDescription.replaceAll("</b>", "");

        return htmlDescription;
    }

    public String getDate() { return date; }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRecap() {
        return recap;
    }
}

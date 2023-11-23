package com.example.meccanocar.model;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Meccanocar {
    private static String URL = "https://www.meccanocar.fr/";
    private String newsUrl = "/fr/news";
    private Catalog catalog;
    private ArrayList<News> news;
    private ArrayList<Item> last5ItemsViewed;
    private Context context;

    public Meccanocar(Context context) {
        this.context = context;
        this.catalog = new Catalog(URL);
        this.news = new ArrayList<>();
        this.last5ItemsViewed = new ArrayList<>();
        loadLast5ItemsViewedFromJson(); // On récupère les 5 derniers items vus depuis le fichier JSON
        getNewsFromHttp(); // On récupère les news depuis le site
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public ArrayList<News> getNews() {
        return this.news;
    }

    public ArrayList<Item> getLast5ItemsViewed() {
        return last5ItemsViewed;
    }

    public void saveLast5ItemsViewedToJson() {
        // Convertir last5ItemsViewed en format JSON
        Gson gson = new Gson();
        String json = gson.toJson(last5ItemsViewed);

        // Écrire le JSON dans un fichier
        File path = context.getExternalFilesDir("");

        if (path != null) {
            File file = new File(path, "last5items.json");
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(json);
                fileWriter.flush(); // Assurez-vous que les données sont bien écrites
                fileWriter.close(); // Fermez le FileWriter
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadLast5ItemsViewedFromJson() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Item>>() {
        }.getType();

        // Lecture du JSON depuis le fichier
        File path = context.getExternalFilesDir("");
        if (path != null) {
            File file = new File(path, "last5items.json");
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                last5ItemsViewed = gson.fromJson(bufferedReader, listType);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (last5ItemsViewed == null) {
            last5ItemsViewed = new ArrayList<>();
        }
    }

    public void updateLast5ItemsViewed(Item item) {
        // Ajouter l'item à la liste
        if (!last5ItemsViewed.contains(item)) {
            last5ItemsViewed.add(item);
        }

        // Supprimer le premier item de la liste si elle contient plus de 5 items
        if (last5ItemsViewed.size() > 5) {
            last5ItemsViewed.remove(0);
        }

        // Sauvegarder la liste dans le fichier JSON
        saveLast5ItemsViewedToJson();
    }

    public void getNewsFromHttp() {
        // Créez un client HTTP
        OkHttpClient client = new OkHttpClient();

        // Créez une requête pour récupérer le contenu HTML du site
        Request request = new Request.Builder()
                .url(URL + this.newsUrl)
                .build();

        // Exécutez la requête de manière asynchrone
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Document doc = Jsoup.parse(response.body().string());

                    Elements newsArticles = doc.select(".news-articles-item");

                    for (Element article : newsArticles) {
                        // Récupération de l'image
                        Element image = article.selectFirst(".news-articles-item-small-image");
                        String imageUrl = image.attr("src");
                        System.out.println("[News]Image URL: " + URL + imageUrl);

                        // Récupération de la date
                        Element dateElement = article.selectFirst(".date-and-category-date");
                        String date = dateElement.text();
                        System.out.println("[News]Date: " + date);

                        // Récupération du titre
                        Element titleElement = article.selectFirst(".news-articles-item-title");
                        String title = titleElement.text();
                        System.out.println("[News]Title: " + title);

                        // Récupération du résumé
                        Element recapElement = article.selectFirst(".news-articles-item-content");
                        String recap = recapElement.text();
                        System.out.println("[News]Recap: " + recap);

                        // Récupération du lien pour la description détaillée
                        Element descriptionLink = article.selectFirst(".news-articles-item-body a");
                        String descriptionUrl = descriptionLink.attr("href");

                        // On recupère la description détaillée
                        /*String[] description = getNewsDescriptionHttp(descriptionUrl);
                        System.out.println("[News]Description : " + description);
                        news.add(new News(title, date, URL + imageUrl, recap, description));*/

                        getNewsDescriptionHttp(descriptionUrl, new DescriptionCallback() {
                            @Override
                            public void onDescriptionLoaded(String[] descriptions) {
                                // Utilisez les descriptions récupérées ici
                                String[] description = new String[descriptions.length];
                                /*int i = 0;
                                for (String desc : descriptions) {
                                    System.out.println("[News] Paragraphe : " + desc);
                                    System.out.println("[News] Lien : " + descriptionUrl);
                                    description[i] = desc;
                                    i++;
                                }*/
                                System.out.println("[News] Lien : " + descriptionUrl);
                                description = descriptions;
                                news.add(new News(title, date, URL + imageUrl, recap, description));
                            }
                        });
                    }
                }
            }
        });
    }

    /*public String[] getNewsDescriptionHttp(String u) {
        final String[][] description = {null};

        // Créez un client HTTP
        OkHttpClient client = new OkHttpClient();

        // Créez une requête pour récupérer le contenu HTML du site
        Request request = new Request.Builder()
                .url(u)
                .build();

        // Exécutez la requête de manière asynchrone
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Document doc = Jsoup.parse(response.body().string());

                    // Récupération des paragraphes
                    Element paragraphs = doc.selectFirst(".news-article-content");
                    Elements p = paragraphs.select("p");

                    description[0] = new String[p.size()];
                    int i = 0;

                    // On parcourt les paragraphes
                    for (Element p1 : p) {
                        description[0][i] = p1.text();
                        i++;
                        System.out.println("[News]Paragraphe : " + p1.text());
                    }
                }
            }
        });

        return description[0];
    }*/

    public void getNewsDescriptionHttp(String u, DescriptionCallback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(u)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            /*public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Document doc = Jsoup.parse(response.body().string());

                    Element paragraphs = doc.selectFirst(".news-article-content");
                    Elements p = paragraphs.select("p");

                    String[] descriptions = new String[p.size()];
                    int i = 0;

                    for (Element p1 : p) {
                        descriptions[i] = p1.text();

                        // Si le paragraphe contient <br> on ajoute un saut de ligne
                        if (p1.selectFirst("br") != null) {
                            descriptions[i] += "\n";
                            System.out.println("[News]Saut de ligne : " + descriptions[i]);
                        }
                        i++;
                    }

                    callback.onDescriptionLoaded(descriptions);
                }
            }*/
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Document doc = Jsoup.parse(response.body().string());

                    Element paragraphs = doc.selectFirst(".news-article-content");
                    Elements p = paragraphs.select("p");

                    String[] descriptions = new String[p.size()];
                    for (Element p1 : p) {
                        if (p1.html() != "null") {
                            descriptions[0] += p1.html();
                        }
                    }

                    System.out.println("[News]Description : " + descriptions[0]);

                    /*for (int i = 0; i < p.size(); i++) {
                        Element paragraph = p.get(i);
                        String paragraphText = paragraph.text();

                        // Si le paragraphe contient des sauts de ligne
                        if (paragraph.selectFirst("br") != null) {
                            paragraphText += "\n";
                        } else {
                            // Ajoute un saut de ligne entre les paragraphes s'il n'y a pas de balise <br>
                            paragraphText += "\n\n";
                        }

                        descriptions[i] = paragraphText;
                    }*/

                    callback.onDescriptionLoaded(descriptions);
                }
            }
        });
    }
}
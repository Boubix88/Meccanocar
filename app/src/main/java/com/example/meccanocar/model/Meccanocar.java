package com.example.meccanocar.model;

import android.content.Context;

import com.example.meccanocar.model.listener.DescriptionCallback;
import com.example.meccanocar.model.listener.NewsLoadListener;
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

    public void getNewsFromHttp(final NewsLoadListener listener) {
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
                        getNewsDescriptionHttp(descriptionUrl, new DescriptionCallback() {
                            @Override
                            public void onDescriptionLoaded(String[] descriptions) {
                                // Utilisez les descriptions récupérées ici
                                String[] description = new String[descriptions.length];
                                System.out.println("[News] Lien : " + descriptionUrl);
                                description = descriptions;
                                news.add(new News(title, date, URL + imageUrl, recap, description));
                            }
                        });
                    }

                    System.out.println("[HomeFragment] chargement new terminé ");
                    // Après avoir collecté toutes les nouvelles
                    // Informez le listener que le chargement est terminé
                    listener.onNewsLoaded();
                }
            }
        });
    }

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

                    callback.onDescriptionLoaded(descriptions);
                }
            }
        });
    }
}
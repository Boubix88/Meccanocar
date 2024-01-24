package com.example.meccanocar.model;

import android.content.Context;

import com.example.meccanocar.model.listener.CatalogLoadListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Catalog implements Serializable {
    private ArrayList<Category> catalog;
    private String url;
    private String catalogUrl = "/fr/catalogues";
    private Context context;

    public Catalog(String u, Context context){
        this.catalog = new ArrayList<>();
        this.url = u;
        this.context = context;
    }

    public void getCatalogFromHttp(final CatalogLoadListener listener){
        // Créez un client HTTP
        OkHttpClient client = new OkHttpClient();

        // Créez une requête pour récupérer le contenu HTML du site
        Request request = new Request.Builder()
                .url(this.url + this.catalogUrl)
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
                    // Récupérez le contenu HTML de la réponse
                    String html = response.body().string();

                    // Utilisez JSoup pour extraire les informations nécessaires
                    Document doc = Jsoup.parse(html);
                    // Ajoutez votre logique d'extraction de données ici

                    // Exemple d'extraction de titre de la page
                    String title = doc.title();
                    System.out.println("[test]Titre de la page : " + title);


                    /* Récupération des catégories sans items */

                    Elements catalogElementWithoutItems = doc.select(".launch-tile");

                    // Parcours de tous les éléments "launch-tile"
                    int i = 0;
                    for (Element categoryElementWithoutItems : catalogElementWithoutItems) {
                        // Récupération du titre (premier élément h3)
                        Element titleCategoryWithoutItems = categoryElementWithoutItems.selectFirst(".launch-tile-label");
                        String categoryNameWithoutItems = titleCategoryWithoutItems.text();
                        System.out.println("[test]Titre categorie : " + categoryNameWithoutItems);

                        // Récupération de l'élément <div> contenant l'image
                        Element imageDiv = categoryElementWithoutItems.selectFirst(".launch-tile-image-hover");

                        // Récupération du style de l'élément <div>
                        String srcAttribute = imageDiv.attr("src");
                        System.out.println("[test]Image URL : " + url + srcAttribute);

                        // On crée une nouvelle catégorie sans items
                        Category category = new Category(categoryNameWithoutItems, url + srcAttribute, i);

                        // On crée un nouvel item avec son nom et le lien de son pdf
                        Element linkElement = categoryElementWithoutItems.selectFirst(".launch-tile-label-link");
                        String itemLink = linkElement.attr("href");
                        category.addSubCategory(new SubCategory(categoryNameWithoutItems, url + itemLink, i, context));

                        catalog.add(category);
                        i++;
                    }


                    /* Récupération des catégories avec items */

                    // Récupération de tous les éléments avec la classe "all-products-section"
                    Elements catalogElement = doc.select(".all-products-section");

                    // Parcours de tous les éléments "all-products-section"
                    for (Element categoryElement : catalogElement) {
                        // Récupération du titre (premier élément h3)
                        Element titleCategory = categoryElement.selectFirst("h3");
                        String categoryName = titleCategory.text();
                        System.out.println("[test]h3 : " + categoryName);


                        // Récupération de l'élément <div> contenant l'image
                        Element imageDiv = categoryElement.selectFirst(".all-products-section-image");

                        // Récupération du style de l'élément <div>
                        String styleAttribute = imageDiv.attr("style");

                        // Utilisation d'une expression régulière pour extraire l'URL de l'image
                        String imageUrl = styleAttribute.replaceAll(".*?url\\('(.*?)'\\).*", "$1");

                        System.out.println("[test]Image URL : " + url + imageUrl);


                        // On crée une nouvelle catégorie
                        Category category = new Category(categoryName, url + imageUrl, i);

                        // Récupération des éléments <a> sous le div
                        Elements linkElements = categoryElement.select("div.product-link-wrapper a");

                        // Parcours des éléments <a> pour obtenir les titres et les liens
                        for (Element itemElement : linkElements) {
                            String itemName = itemElement.text();
                            String itemLink = itemElement.attr("href");

                            System.out.println("[test]Titre : " + itemName);
                            System.out.println("[test]Lien : " + url + itemLink);

                            // On crée une nouvelle sous catégorie son nom et le lien de son pdf
                            SubCategory subCategory = new SubCategory(itemName, url + itemLink, i, context);

                            // On ajoute la sous catégorie à la catégorie
                            category.addSubCategory(subCategory);
                        }

                        // Id de la catégorie
                        i++;

                        // On ajoute cette nouvelle catégorie à notre catalogue
                        catalog.add(category);
                    }

                    listener.onCatalogLoaded();
                }
            }
        });
    }

    public ArrayList<Category> getCatalog() {
        return catalog;
    }

    public Category getCategoryByName(String selectedCategoryName) {
        for (Category category : catalog) {
            if (category.getName().equals(selectedCategoryName)) {
                return category;
            }
        }

        return null;
    }

    public List<SubCategory> getAllSubCatgegorys() {
        List<SubCategory> allSubCategories = new ArrayList<>();
        for (Category category : catalog) {
            allSubCategories.addAll(category.getSubCategorys());
        }
        return allSubCategories;
    }
}
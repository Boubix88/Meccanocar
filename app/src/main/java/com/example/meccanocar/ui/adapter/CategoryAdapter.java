package com.example.meccanocar.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.catalog.CategoryViewHolder;
import com.example.meccanocar.utils.SimilarityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Category> categories;
    private final List<Category> allCategories;
    private final TextView searchResult;

    public CategoryAdapter(List<Category> categories, View parent) {
        this.categories = categories;
        this.allCategories = new ArrayList<>(categories); // Créez une copie de toutes les catégories

        // Récupérer la référence de la SearchView dans le fragment
        SearchView searchView = parent.findViewById(R.id.searchView);

        // Récupérer la référence du TextView dans le fragment
        searchResult = parent.findViewById(R.id.text_search_no_result);
        searchResult.setVisibility(View.GONE);

        // On affiche les categrories/items dont le texte correspond à la recherche
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCategories(newText);
                return true;
            }
        });
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_card, parent, false);

        return new CategoryViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.afficher(category);
    }

    /**
     * Récuperer le nombre de catégorie
     * @return le nombre de catégorie
     */
    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        return 0;
    }

    /**
     * Filtrer les catégories en fonction de la requête
     *
     * @param query la requête
     */
    public void filterCategories(String query) {
        categories.clear();
        searchResult.setVisibility(View.GONE); // Cacher le TextView
        double maxWordSimilarity = 1.0;

        if (query.isEmpty()) {
            categories.addAll(allCategories);
        } else {
            query = query.toLowerCase();
            for (Category category : allCategories) {
                double maxSimilarity = 0.0;
                for (String categoryNameWord : category.getName().toLowerCase().split("\\s+")) {
                    // Calcul de la similarité entre chaque mot de la catégorie et la requête
                    double wordSimilarity = SimilarityUtils.jaccardSimilarity(categoryNameWord, query);
                    if (wordSimilarity > maxSimilarity) {
                        maxSimilarity = wordSimilarity;
                    }
                }

                if (maxWordSimilarity < maxSimilarity && maxSimilarity > 0.80) {
                    maxWordSimilarity = maxSimilarity;
                }

                // Calcul de la similarité entre le nom complet catégorie et la requête
                double maxSimilarity2 = SimilarityUtils.jaccardSimilarity(category.getName().toLowerCase(), query);
                System.out.println("Test///  mot = " + maxSimilarity + "   Max = " + maxWordSimilarity + " --  " + category.getName() + " = " + maxSimilarity2 + "  >  " + calculateRandomWordPercentage(category.getName())/100);

                // Si la similarité maximale est supérieure à un certain seuil, ajouter la catégorie
                /*if (maxSimilarity > 0.8 || (maxWordSimilarity > 0.8 && maxSimilarity2 > calculateRandomWordPercentage(category.getName())/100)) { // Seuil de similarité de 80% pour un mot et 50% pour le nom complet
                    categories.add(category);
                }*/
                if (maxSimilarity > 0.6 || maxSimilarity2 > (1.0 - calculateRandomWordPercentage(category.getName())/100)) {
                    categories.add(category);
                }
            }
        }

        // Si la liste des catégories est vide, afficher le TextView
        if (categories.isEmpty()){
            searchResult.setVisibility(View.VISIBLE); // Afficher le TextView
        }
        notifyDataSetChanged(); // Mettre à jour le RecyclerView avec les catégories filtrées
    }

    /**
     * Calculez le pourcentage d'apparition d'un mot aléatoire dans une chaîne de caractères
     *
     * @param text la chaîne de caractères
     * @return le pourcentage d'apparition d'un mot aléatoire dans la chaîne de caractères
     */
    public static double calculateRandomWordPercentage(String text) {
        String[] words = text.split("\\s+");
        int totalWords = words.length;

        if (totalWords == 0) {
            return 0.0;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(totalWords);
        String randomWord = words[randomIndex];

        int wordCount = 0;
        for (String word : words) {
            if (word.equalsIgnoreCase(randomWord)) {
                wordCount++;
            }
        }

        if (wordCount == 1) {
            return 0.0;
        }

        return (double) wordCount / totalWords * 100;
    }
}
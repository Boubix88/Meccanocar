package com.example.meccanocar.ui.catalog;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.adapter.CategoryAdapter;
import com.example.meccanocar.ui.adapter.ItemAdapter;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final ImageView image;
    private final ImageView imageArrow;
    private final RecyclerView itemRecyclerView;
    //private final SearchView searchView;
    private final CategoryAdapter categoryAdapter;

    public CategoryViewHolder(final View itemView, CategoryAdapter adapter) {
        super(itemView);

        name = itemView.findViewById(R.id.nameCategory);
        image = itemView.findViewById(R.id.imageCategory);
        itemRecyclerView = itemView.findViewById(R.id.itemRecyclerView);
        imageArrow = itemView.findViewById(R.id.imageBottomArrow);
        categoryAdapter = adapter;

        // On récupère la SearchView dans fragment_catalog.xml
        /*System.out.println("itemView.getRootView().getRootView() : " + itemView);
        searchView = itemView.getRootView().getRootView().findViewById(R.id.searchView); // Récupérer la référence de la SearchView dans le fragment*/

        // On cache les items de la catégorie
        itemRecyclerView.setVisibility(View.GONE);

        // On affiche les items de la catégorie si on clique sur la catégorie
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On affiche les items de la catégorie si ils sont cachés, sinon on les cache
                if (itemRecyclerView.getVisibility() == View.GONE) {
                    expandRecyclerView(itemRecyclerView);
                    rotateArrow(imageArrow, 0f, 180f); // Rotation vers le haut (180 degrés)
                } else {
                    collapseRecyclerView(itemRecyclerView);
                    rotateArrow(imageArrow, 180f, 0f); // Rotation vers le bas (180 degrés)
                }
            }
        });

        // On affiche les categrories/items dont le texte correspond à la recherche
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateList(newText);
                return true;
            }
        });*/
    }

    private void rotateArrow(ImageView imageView, float fromDegrees, float toDegrees) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegrees,
                toDegrees,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        rotateAnimation.setDuration(500); // Durée de l'animation en millisecondes
        rotateAnimation.setFillAfter(true); // Conserve la position finale de l'animation

        imageView.startAnimation(rotateAnimation);
    }

    private void expandRecyclerView(final View view) {
        view.setVisibility(View.VISIBLE);
        Animation slideDown = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_down);
        view.startAnimation(slideDown);
    }

    private void collapseRecyclerView(final View view) {
        Animation slideUp = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_up);
        slideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(slideUp);
    }

    private void updateList(String query) {
        categoryAdapter.filterCategories(query);
    }

    public void afficher(Category category) {
        name.setText(category.getName());
        Picasso.get().load(category.getUrlImage()).into(image);

        // Mettez en place l'adaptateur pour le RecyclerView des articles
        ItemAdapter itemAdapter = new ItemAdapter(category.getItems());
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        itemRecyclerView.setAdapter(itemAdapter);
    }
}
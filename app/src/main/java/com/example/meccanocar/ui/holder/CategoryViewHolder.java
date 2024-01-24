package com.example.meccanocar.ui.holder;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.adapter.CategoryAdapter;
import com.example.meccanocar.ui.adapter.SubCategoryAdapter;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final ImageView image;
    private final ImageView imageArrow;
    private final RecyclerView subCategoryRecyclerView;
    //private final SearchView searchView;
    private final CategoryAdapter categoryAdapter;
    private View root;

    public CategoryViewHolder(final View itemView, View root, CategoryAdapter adapter) {
        super(itemView);

        this.root = root;

        name = itemView.findViewById(R.id.nameCategory);
        image = itemView.findViewById(R.id.imageCategory);
        subCategoryRecyclerView = itemView.findViewById(R.id.subCategoryRecyclerView);
        imageArrow = itemView.findViewById(R.id.imageBottomArrow);
        categoryAdapter = adapter;

        // On récupère la SearchView dans fragment_catalog.xml
        /*System.out.println("itemView.getRootView().getRootView() : " + itemView);
        searchView = itemView.getRootView().getRootView().findViewById(R.id.searchView); // Récupérer la référence de la SearchView dans le fragment*/

        // On cache les items de la catégorie
        subCategoryRecyclerView.setVisibility(View.GONE);

        // On affiche les items de la catégorie si on clique sur la catégorie
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On affiche les items de la catégorie si ils sont cachés, sinon on les cache
                if (subCategoryRecyclerView.getVisibility() == View.GONE) {
                    expandRecyclerView(subCategoryRecyclerView);
                    rotateArrow(imageArrow, 0f, 180f); // Rotation vers le haut (180 degrés)
                } else {
                    collapseRecyclerView(subCategoryRecyclerView);
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
        // On affiche les items de la catégorie avec une animation
        view.setVisibility(View.VISIBLE);
        Animation slideDown = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.slide_down);
        view.startAnimation(slideDown);


        // RecyclerView globale contenue dans le fragment
        RecyclerView recyclerView = (RecyclerView) itemView.getParent();

        // Récupérer la position de la catégorie cliquée
        int positionOfClickedCategory = getAdapterPosition();

        // On récupère les informations de la catégorie cliquée
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        // On vérifie que la position de la catégorie cliquée n'est pas la dernière
        if (positionOfClickedCategory < layoutManager.getItemCount()) {
            // On récupère la hauteur de la RecyclerView presente dans la catégorie cliquée
            recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int recyclerViewHeight = view.getHeight();

            // On adapte la transition slide_down en fonction de la hauteur de la RecyclerView
            Animation slideDownCat = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.ABSOLUTE, -recyclerViewHeight,
                    Animation.ABSOLUTE, 0f
            );
            slideDownCat.setDuration(itemView.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));

            // On applique l'animation sur toutes les catégories suivantes
            for (int i = positionOfClickedCategory; i < layoutManager.getItemCount() - 1; i++) {
                // Récupérer la vue de la catégorie suivante
                View viewTmp = layoutManager.findViewByPosition(i + 1);

                viewTmp.startAnimation(slideDownCat);
            }
        }
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


        // RecyclerView globale contenue dans le fragment
        RecyclerView recyclerView = (RecyclerView) itemView.getParent();

        // Récupérer la position de la catégorie cliquée
        int positionOfClickedCategory = getAdapterPosition();

        // On récupère les informations de la catégorie cliquée
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        // On vérifie que la position de la catégorie cliquée n'est pas la dernière
        if (positionOfClickedCategory < layoutManager.getItemCount()) {
            // On récupère la hauteur de la RecyclerView presente dans la catégorie cliquée
            recyclerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int recyclerViewHeight = view.getHeight();

            // On adapte la transition slide_down en fonction de la hauteur de la RecyclerView
            Animation slideUpCat = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.ABSOLUTE, 0f,
                    Animation.ABSOLUTE, -recyclerViewHeight
            );
            slideUpCat.setDuration(itemView.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));

            // On applique l'animation sur toutes les catégories suivantes
            for (int i = positionOfClickedCategory; i < layoutManager.getItemCount() - 1; i++) {
                System.out.println("[Category] Anim i : " + i);
                // Récupérer la vue de la catégorie suivante
                View viewTmp = layoutManager.findViewByPosition(i + 1);

                // On vérifie que la vue n'est pas null
                if (viewTmp != null) {
                    viewTmp.startAnimation(slideUpCat);
                } else {
                    System.out.println("[Category] ViewTmp null");
                }
            }
        }
    }

    private void updateList(String query) {
        categoryAdapter.filterCategories(query);
    }

    public void afficher(Category category) {
        // On affiche le nom et l'image de la catégorie
        name.setText(category.getName());
        category.loadImage(image);

        // Mettez en place l'adaptateur pour le RecyclerView des articles
        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(category.getSubCategorys(), root);
        subCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        subCategoryRecyclerView.setAdapter(subCategoryAdapter);
    }
}
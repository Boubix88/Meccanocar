package com.example.meccanocar.ui.catalog;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.adapter.ItemAdapter;
import com.example.meccanocar.ui.adapter.OnCategoryClickListener;
import com.squareup.picasso.Picasso;

// CategoryViewHolder.java
// CategoryViewHolder.java
public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final ImageView image;
    private final RecyclerView itemRecyclerView;
    private Category category;

    public CategoryViewHolder(final View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nameCategory);
        image = itemView.findViewById(R.id.imageCategory);
        itemRecyclerView = itemView.findViewById(R.id.itemRecyclerView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lancez l'activité des détails de la catégorie avec les items
                /*Intent intent = new Intent(itemView.getContext(), CategoryDetailActivity.class);
                intent.putExtra("category", category);
                itemView.getContext().startActivity(intent);*/
            }
        });
    }

    public void afficher(Category category) {
        this.category = category;
        name.setText(category.getName());
        Picasso.get().load(category.getUrlImage()).into(image);

        // Mettez en place l'adaptateur pour le RecyclerView des articles
        ItemAdapter itemAdapter = new ItemAdapter(category.getItems());
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        itemRecyclerView.setAdapter(itemAdapter);
    }
}

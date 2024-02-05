package com.example.meccanocar.ui.holder;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.SubCategory;
import com.example.meccanocar.model.manager.MeccanocarManager;
import com.example.meccanocar.ui.adapter.ItemAdapter;
import com.example.meccanocar.ui.catalog.ItemsDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class LastItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName;
    private final ImageView image;

    public LastItemViewHolder(@NonNull View lastItemView, @NonNull View root, List<Item> items) {
        super(lastItemView);
        itemName = lastItemView.findViewById(R.id.nameItem); // Remplacez avec l'ID réel de l'élément dans votre item_card.xml

        image = lastItemView.findViewById(R.id.imageItem);  // Ajout de cette ligne

        // On remplace la recyclerView des categories par la gridView des items
        GridView gridView = root.findViewById(R.id.itemsGridView); // Ajout de cette ligne

        RecyclerView recyclerView = root.findViewById(R.id.categoryRecyclerView);

        // Ajout du listener sur chaque item
        lastItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupération de l'item
                Item item = items.get(getAdapterPosition());

                // Affichage de l'item
                Intent intent = new Intent(view.getContext(), ItemsDetailsActivity.class);
                intent.putExtra("item", item);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void afficher(Item item) {
        itemName.setText(item.getName()); // Remplacez avec la méthode appropriée pour obtenir le nom de l'élément
        image.setImageBitmap(item.getBitmap()); // Remplacez avec la méthode appropriée pour obtenir l'image de l'élément
    }
}

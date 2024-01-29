package com.example.meccanocar.ui.catalog;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.News;

public class ItemsDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        // Masquer la barre d'action
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Récupérer les références des vues pour afficher les détails
        imageView = findViewById(R.id.imageViewItemsDetails);
        titleTextView = findViewById(R.id.titleItemsDetails);
        descriptionTextView = findViewById(R.id.descriptionItemsDetails);

        // Récupérer l'ID de l'item depuis l'intent
        if (getIntent() != null && getIntent().hasExtra("item")) {
            Item item = (Item) getIntent().getParcelableExtra("item");

            // Afficher les détails récupérés dans les vues
            if (item != null) {
                // On affiche l'image de l'item, la date, le titre, le résumé et la description et la description détaillée
                imageView.setImageBitmap(item.getBitmap());
                titleTextView.setText(item.getName());

                String tmp = item.getDescription().replaceAll("•", "\n\n•  ");

                descriptionTextView.setText(tmp);
            }
        }

        // Gestion du bouton retour
        ImageView imageViewBack = findViewById(R.id.backButton);
        imageViewBack.setOnClickListener(view -> finish());

        // Gestion du texte clicable pour retourner en arrière
        TextView textViewBack = findViewById(R.id.textViewBack);
        textViewBack.setOnClickListener(view -> finish());
    }
}

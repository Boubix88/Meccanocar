package com.example.meccanocar.ui.home;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meccanocar.R;
import com.example.meccanocar.model.News;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView dateTextView;
    private TextView titleTextView;
    private TextView recapTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        // Masquer la barre d'action
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Récupérer les références des vues pour afficher les détails
        imageView = findViewById(R.id.imageViewNewsDetails);
        dateTextView = findViewById(R.id.dateNewsDetails);
        titleTextView = findViewById(R.id.titleNewsDetails);
        recapTextView = findViewById(R.id.recapNewsDetails);
        descriptionTextView = findViewById(R.id.descriptionNewsDetails);

        // Récupérer l'ID de la news depuis l'intent
        if (getIntent() != null && getIntent().hasExtra("news")) {
            News news = (News) getIntent().getSerializableExtra("news");

            // Afficher les détails récupérés dans les vues
            if (news != null) {
                // Utiliser les données de l'objet News pour remplir les vues
                Picasso.get().load(news.getImageUrl()).into(imageView); // Remplacez avec la méthode appropriée pour définir l'image
                dateTextView.setText(news.getDate());
                titleTextView.setText(news.getTitle());
                recapTextView.setText(news.getRecap());
                descriptionTextView.setText(news.getFullDescription());
            }
        }

        // Gestion du bouton retour
        ImageView imageViewBack = findViewById(R.id.backButton);
        imageViewBack.setOnClickListener(view -> finish());

        // Gestion du texte clicable pour retourner en arrière
        TextView textViewBack = findViewById(R.id.textViewBack);
        textViewBack.setOnClickListener(view -> finish());
    }

    // Simule la récupération des détails de la news
    private News getNewsDetails(int newsId) {
        // Ici, vous devriez récupérer les détails de la news depuis votre source de données
        // Remplacez cette méthode avec votre logique de récupération de données
        // Utilisez l'ID pour obtenir les détails spécifiques de la news
        // Retournez un objet News ou null si non trouvé
        // Par exemple :
        // return MeccanocarManager.getInstance().getNewsDetails(newsId);
        return null; // Pour l'exemple, retourne null
    }
}
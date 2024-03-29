package com.example.meccanocar.ui.home;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meccanocar.R;
import com.example.meccanocar.utils.Language;
import com.example.meccanocar.model.News;
import com.example.meccanocar.model.manager.MeccanocarManager;
import com.example.meccanocar.utils.Settings;

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

        // On set la langue de l'application
        Language.setLanguage(getResources());

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
                // On affiche l'image de la news, la date, le titre, le résumé et la description et la description détaillée
                news.loadImage(imageView);
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

    @Override
    public Resources.Theme getTheme() {
        Settings settings = MeccanocarManager.getInstance().getSettings();
        Resources.Theme theme = super.getTheme();

        switch (settings.getTheme()) {
            case "light":
                theme.applyStyle(R.style.Theme_MeccanocarLight, true);
                break;
            case "dark":
                theme.applyStyle(R.style.Theme_MeccanocarDark, true);
                break;
            case "auto":
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        theme.applyStyle(R.style.Theme_MeccanocarDark, true);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        theme.applyStyle(R.style.Theme_MeccanocarLight, true);
                        break;
                }
                break;
        }

        return super.getTheme();
    }
}
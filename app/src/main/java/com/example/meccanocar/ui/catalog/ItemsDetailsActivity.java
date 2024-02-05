package com.example.meccanocar.ui.catalog;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.utils.Language;
import com.example.meccanocar.model.manager.MeccanocarManager;
import com.example.meccanocar.utils.Settings;

public class ItemsDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        // On set la langue de l'application
        Language.setLanguage(getResources());

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

                if (!tmp.isEmpty()) {
                    descriptionTextView.setText(tmp);
                } else {
                    descriptionTextView.setText("\n\n" + getString(R.string.no_description));
                }

                // On set le tableau de référence
                setTabRef(item.getTabRef());
            }
        }

        // Gestion du bouton retour
        ImageView imageViewBack = findViewById(R.id.backButton);
        imageViewBack.setOnClickListener(view -> finish());

        // Gestion du texte clicable pour retourner en arrière
        TextView textViewBack = findViewById(R.id.textViewBack);
        textViewBack.setOnClickListener(view -> finish());
    }

    public void setTabRef(String[][] tabRef) {
        // Récupérer la référence du TableLayout
        TableLayout tableLayout = findViewById(R.id.tableLayoutItemsDetails);

        // Boucle à travers les colonnes (changement ici)
        for (int j = 0; j < tabRef[0].length; j++) {
            TableRow tableRow = new TableRow(this);

            // Boucle à travers les lignes (changement ici)
            for (int i = 0; i < tabRef.length; i++) {
                TextView textView = new TextView(this);
                if (j == 0) {
                    textView.setBackground(getDrawable(R.drawable.tableborderheader));
                    // On met le texte en gras
                    textView.setTextAppearance(R.style.frag1HeaderCol);
                } else {
                    textView.setBackground(getDrawable(R.drawable.tableborder));
                }
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(14);

                // Configurez le style et le texte du TextView
                textView.setText(tabRef[i][j]);

                // Ajoutez le TextView à la ligne actuelle
                tableRow.addView(textView);
            }

            // Ajoutez la ligne au TableLayout
            tableLayout.addView(tableRow);
        }
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

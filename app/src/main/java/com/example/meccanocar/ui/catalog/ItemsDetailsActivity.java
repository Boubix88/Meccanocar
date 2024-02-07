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

import java.util.ArrayList;

public class ItemsDetailsActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    private ArrayList<TableLayout> tableLayouts = new ArrayList<>();
    private int indexTab;

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

        indexTab = 0;

        // Récupérer la référence du TableLayout
        TableLayout tableLayout = findViewById(R.id.tableLayoutItemsDetails);

        // On recupère la référence du tableau de caractéristiques
        TableLayout tableLayoutCara = findViewById(R.id.tableLayoutCaracteristic);

        // On ajoute les tableaux à la liste
        tableLayouts.add(tableLayout);
        tableLayouts.add(tableLayoutCara);

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
        // Boucle à travers les lignes
        for (int j = 0; j < tabRef[0].length; j++) {
            System.out.println("[test] Case : " + tabRef[0][j]);
            TableRow tableRow = new TableRow(this);

            // On change de tableau si la première case est vide
            if (tabRef[0][j].isEmpty()) {
                indexTab++;
            }

            // Boucle à travers les colonnes
            for (int i = 0; i < tabRef.length; i++) {
                // On saute la case si elle est vide
                if (tabRef[i][j].isEmpty()) {
                    continue;
                }

                TextView textView = new TextView(this);

                // Première ligne du tableau de reférence ou deuxieme ligne du tableau de pièces de rechange
                if (j == 0 && tabRef[0][0].equals("Code") || (j - 1 >= 0 && tabRef[0][j - 1].contains("Pièces"))) {
                    textView.setBackground(getDrawable(R.drawable.table_border_header_ref));
                    // On met le texte en gras
                    textView.setTextAppearance(R.style.frag1HeaderCol);
                } else if (tabRef[0][j].contains("Caractéristiques techniques")) { // Premiere ligne du tableau de caractéristiques
                    textView.setBackground(getDrawable(R.drawable.table_border_header_caracteristic));
                    // On met le texte en gras
                    textView.setTextAppearance(R.style.frag1HeaderCol);
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                    layoutParams.span = tabRef.length; // La première colonne prend 2 cases
                    textView.setLayoutParams(layoutParams);
                } else if (tabRef[0][j].contains("Pièces")) { // Premiere ligne du tableau de pièces de rechange
                    textView.setBackground(getDrawable(R.drawable.table_border_header_piece));
                    // On met le texte en gras
                    textView.setTextAppearance(R.style.frag1HeaderCol);
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                    layoutParams.span = tabRef.length; // La première colonne prend 2 cases
                    textView.setLayoutParams(layoutParams);
                } else { // cases normales
                    textView.setBackground(getDrawable(R.drawable.tableborder));
                }

                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(10);

                // Configurez le style et le texte du TextView
                textView.setText(tabRef[i][j]);

                // Ajoutez le TextView à la ligne actuelle
                tableRow.addView(textView);
            }

            // Ajoutez la ligne au TableLayout
            tableLayouts.get(indexTab).addView(tableRow);
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

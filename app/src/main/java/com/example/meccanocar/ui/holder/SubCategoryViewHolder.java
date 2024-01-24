package com.example.meccanocar.ui.holder;

// SubCategoryViewHolder.java
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName;
    private final ImageView downloadButton;
    public SubCategoryViewHolder(@NonNull View subCategoryView, @NonNull View root, List<SubCategory> subCategories) {
        super(subCategoryView);
        itemName = subCategoryView.findViewById(R.id.itemName); // Remplacez avec l'ID réel de l'élément dans votre item_card.xml

        downloadButton = subCategoryView.findViewById(R.id.downloadButton);  // Ajout de cette ligne

        // On remplace la recyclerView des categories par la gridView des items
        GridView gridView = root.findViewById(R.id.itemsGridView); // Ajout de cette ligne

        RecyclerView recyclerView = root.findViewById(R.id.categoryRecyclerView);

        // Ajout du listener sur chaque item
        subCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.setVisibility(View.VISIBLE); // Ajout de cette ligne
                recyclerView.setVisibility(View.GONE);

                // Récupérer l'objet SubCategory associé à cet élément de la liste
                // TEST à retirer plus tard et modifier pour afficher les items
                SubCategory subCategory = getSubCategory();
                ArrayList<Item> items = subCategory.getItems();

                // Créer un adaptateur personnalisé (vous devrez le créer)
                ItemAdapter adapter = new ItemAdapter(items);

                // Définir l'adaptateur pour la GridView
                gridView.setAdapter(adapter);

                // Vérifier si l'objet SubCategory et le lien PDF ne sont pas null
                /*if (subCategory != null && subCategory.getLink() != null) {
                    // Ouvrir le PDF avec une application externe
                    openPdfWithExternalApp(itemView.getContext(), subCategory.getLink());

                    // On met a jour la liste des 5 derniers subCategories vus
                    MeccanocarManager.getInstance().updateLast5ItemsViewed(subCategory);
                } else {
                    Toast.makeText(itemView.getContext(), R.string.open_pdf_error, Toast.LENGTH_SHORT).show();
                }*/
            }

            // Méthode pour obtenir l'objet SubCategory associé à cet élément de la liste
            private SubCategory getSubCategory() {
                for (SubCategory subCategory : subCategories) {
                    if (subCategory.getName().equals(itemName.getText())) {
                        return subCategory;
                    }
                }
                return null;
            }
        });

        // Ajout de l'écouteur de clic sur le bouton de téléchargement
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer l'objet SubCategory associé à cet élément de la liste
                SubCategory subCategory = getSubCategory();

                // Vérifier si l'objet SubCategory et le lien PDF ne sont pas null
                if (subCategory != null && subCategory.getLink() != null) {
                    // Déclencher le téléchargement du PDF
                    downloadPdf(subCategory.getLink(), subCategory.getName());

                    // On met a jour la liste des 5 derniers subCategories vus
                    MeccanocarManager.getInstance().updateLast5ItemsViewed(subCategory);

                    Toast.makeText(subCategoryView.getContext(), R.string.download_pdf_text, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(subCategoryView.getContext(), R.string.download_pdf_error, Toast.LENGTH_SHORT).show();
                }
            }

            // Méthode pour obtenir l'objet SubCategory associé à cet élément de la liste
            private SubCategory getSubCategory() {
                for (SubCategory subCategory : subCategories) {
                    if (subCategory.getName().equals(itemName.getText())) {
                        return subCategory;
                    }
                }

                return null;
            }
        });
    }

    // Méthode pour ouvrir le PDF avec une application externe
    private void openPdfWithExternalApp(Context context, String pdfUrl) {
        Uri uri = Uri.parse(pdfUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // Gérer les exceptions, par exemple si aucune application pour les PDF n'est installée
            Toast.makeText(context, R.string.no_pdf_viewer_error, Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour déclencher le téléchargement du PDF
    private void downloadPdf(String pdfUrl, String pdfName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfUrl));
        request.setTitle(pdfName);
        request.setDescription("Downloading PDF");

        // Définir le dossier de destination pour le téléchargement
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pdfName + ".pdf");

        // Obtenir le gestionnaire de téléchargement du système
        DownloadManager downloadManager = (DownloadManager) itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        // Soumettre la demande de téléchargement au gestionnaire de téléchargement
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

    public void afficher(SubCategory subCategory) {
        itemName.setText(subCategory.getName()); // Remplacez avec la méthode appropriée pour obtenir le nom de l'élément
    }
}

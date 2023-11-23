package com.example.meccanocar.ui.holder;

// ItemViewHolder.java
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.Meccanocar;
import com.example.meccanocar.model.MeccanocarManager;

import java.util.List;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName;
    private final ImageView downloadButton;
    public ItemViewHolder(@NonNull View itemView, List<Item> items) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName); // Remplacez avec l'ID réel de l'élément dans votre item_card.xml

        downloadButton = itemView.findViewById(R.id.downloadButton);  // Ajout de cette ligne

        // Ajout du listener sur chaque item
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer l'objet Item associé à cet élément de la liste
                Item item = getItem();

                // Vérifier si l'objet Item et le lien PDF ne sont pas null
                if (item != null && item.getLink() != null) {
                    // Ouvrir le PDF avec une application externe
                    openPdfWithExternalApp(itemView.getContext(), item.getLink());

                    // On met a jour la liste des 5 derniers items vus
                    MeccanocarManager.getInstance().updateLast5ItemsViewed(item);
                } else {
                    Toast.makeText(itemView.getContext(), R.string.open_pdf_error, Toast.LENGTH_SHORT).show();
                }
            }

            // Méthode pour obtenir l'objet Item associé à cet élément de la liste
            private Item getItem() {
                for (Item item : items) {
                    if (item.getName().equals(itemName.getText())) {
                        return item;
                    }
                }
                return null;
            }
        });

        // Ajout de l'écouteur de clic sur le bouton de téléchargement
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer l'objet Item associé à cet élément de la liste
                Item item = getItem();

                // Vérifier si l'objet Item et le lien PDF ne sont pas null
                if (item != null && item.getLink() != null) {
                    // Déclencher le téléchargement du PDF
                    downloadPdf(item.getLink(), item.getName());

                    // On met a jour la liste des 5 derniers items vus
                    MeccanocarManager.getInstance().updateLast5ItemsViewed(item);

                    Toast.makeText(itemView.getContext(), R.string.download_pdf_text, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(itemView.getContext(), R.string.download_pdf_error, Toast.LENGTH_SHORT).show();
                }
            }

            // Méthode pour obtenir l'objet Item associé à cet élément de la liste
            private Item getItem() {
                for (Item item : items) {
                    if (item.getName().equals(itemName.getText())) {
                        return item;
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

    public void afficher(Item item) {
        itemName.setText(item.getName()); // Remplacez avec la méthode appropriée pour obtenir le nom de l'élément
    }
}

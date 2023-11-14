package com.example.meccanocar.ui.catalog;

// ItemViewHolder.java
import android.app.DownloadManager;
import android.content.Context;
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

import java.util.List;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName;
    private final ImageView downloadButton;
    public ItemViewHolder(@NonNull View itemView, List<Item> items) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName); // Remplacez avec l'ID réel de l'élément dans votre item_card.xml

        downloadButton = itemView.findViewById(R.id.downloadButton);  // Ajout de cette ligne

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

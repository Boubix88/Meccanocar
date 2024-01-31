package com.example.meccanocar.model;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DownloadTask extends AsyncTask<String, Void, Void> {
    private Context context;
    private long downloadId;

    public DownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String downloadUrl = params[0];

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), downloadUrl + ".pdf");
        try {
            if (!file.exists()) {
                System.out.println("[test] Téléchargement du pdf : " + downloadUrl);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                request.setTitle(downloadUrl);
                request.setDescription("Downloading PDF");

                // Définir le dossier de destination pour le téléchargement
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, downloadUrl + ".pdf");

                // Obtenir le gestionnaire de téléchargement du système
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                // Soumettre la demande de téléchargement au gestionnaire de téléchargement
                if (downloadManager != null) {
                    downloadManager.enqueue(request);
                }

                file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), downloadUrl + ".pdf");
            }
        } catch (Exception e) {
            Log.e("[test]", "Exception thrown while stripping text", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Téléchargement terminé, effectuez les opérations nécessaires (par exemple, notification, etc.)
        handleDownloadComplete();
    }

    private void handleDownloadComplete() {
        // Effectuez le traitement nécessaire après le téléchargement complet
        // Appelez la méthode pour récupérer les items après le téléchargement complet
        //getItems();
    }
}
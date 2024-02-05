package com.example.meccanocar.model;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.meccanocar.MainActivity;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.cos.COSName;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDResources;
import com.tom_roush.pdfbox.pdmodel.graphics.PDXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.form.PDFormXObject;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubCategory implements Serializable {
    private String name;
    private String link;
    private int id;
    private Context context;
    private ArrayList<Item> items;
    private static String URL = "https://www.meccanocar.fr/";

    public SubCategory(String n, String l, int id, Context context){
        this.name = n;
        this.link = l;
        this.id = id;
        this.context = context;
        this.items = new ArrayList<>();
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }

    public ArrayList<Item> getItems(){
        if (items.isEmpty()) {
            // On récupère les items dans les PDF
            /*  ---- GESTION DU PDF ---- */

            // Si c'est pas un catalogue bizarre
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), this.name + ".pdf");
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            int downloadID = 0;
            // using query method
            boolean finishDownload = false;

            try {
                if (!file.exists()) {
                    System.out.println("[test] Téléchargement du pdf : " + this.name + " "  + this.link);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.link))
                            .setTitle(this.name)
                            .setDescription("Downloading PDF")
                            .setRequiresCharging(false)// Set if charging is required to begin the download
                            .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                            .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                            // On empeche l'ouverture automatique du pdf
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationUri(Uri.fromFile(file));

                    // Définir le dossier de destination pour le téléchargement
                    request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, this.name + ".pdf");

                    // Soumettre la demande de téléchargement au gestionnaire de téléchargement
                    if (downloadManager != null) {
                        // On récupère l'id du téléchargement
                        downloadID = (int) downloadManager.enqueue(request);
                    }

                    file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), this.name + ".pdf");
                } else {
                    finishDownload = true;
                }
            } catch (Exception e) {
                //Log.e("[test]", "Exception thrown while stripping text", e);
                e.printStackTrace();
                return items;
            }

            // On attend que le téléchargement soit terminé
            while (!finishDownload) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadID);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        finishDownload = true;
                    }
                }
                cursor.close();
            }

            PDFBoxResourceLoader.init(context);

            ArrayList<String> itemTitle = new ArrayList<>();
            ArrayList<String> description = new ArrayList<>();
            //ArrayList<String> table = new ArrayList<>();
            ArrayList<Bitmap> images = new ArrayList<>();

            //ArrayList<ArrayList<String>> tableHeader = new ArrayList<>();
            final String[][][] table = {new String[0][0]};
            ArrayList<String[][]> allTable = new ArrayList<>();
            ArrayList<Integer> nbCol = new ArrayList<>();
            ArrayList<Integer> nbRow = new ArrayList<>();

            final int[] indexitem = {-1};
            final int[] indexTableCol = {-1};
            final int[] indexTableRow = {-1};
            final float[] lastheight = {0.0f};
            final float[] lastTextX = {0.0f};
            final float[] lastTextY = {0.0f};


            PDDocument document = null;
            try {
                document = PDDocument.load(file);
            } catch (IOException e) {
                Log.e("[test]", "Exception thrown while loading document to strip", e);
                return items;
            }

            try {
                PDFTextStripper pdfStripper = new PDFTextStripper() {
                    @Override
                    protected void processTextPosition(TextPosition text) {
                        // Supposons que les en-têtes et pieds de page sont dans les 50 points en bas de chaque page
                        float upperLimit = 60.0f;  // Ajustez cela en fonction de vos observations
                        float lowerLimit = 800.0f;  // Les valeurs négatives sont en bas de la page
                        float leftLimit = 30.0f;  // Ajustez cela en fonction de vos observations

                        // Vérifier si la position verticale est dans la zone à exclure
                        float textY = text.getYDirAdj();
                        float textX = text.getXDirAdj();


                        // Ignorer le texte dans la zone exclue
                        if (textY <= upperLimit || textY >= lowerLimit || text.getXDirAdj() <= leftLimit) {
                            return;
                        }

                        super.processTextPosition(text);


                        // nom de l'item
                        if (text.getHeight() > 8.0) {
                            // On mets à jour l'index si on change d'item
                            if (text.getFont().getName().contains("Bold") && lastheight[0] != text.getHeight()) { // ATTENTION derniere condition a l'arrache sinon buga avec tableau dans pdf collage
                                indexitem[0]++;

                                indexTableRow[0] = 0;

                                //tableHeader.removeAll(tableHeader); // on vide le tableau de reference
                                // On reinitialise tableaHeader
                                table[0] = new String[0][0];
                                nbCol.add(0);
                                nbRow.add(0);
                            }

                            if (itemTitle.isEmpty()) {
                                itemTitle.add(indexitem[0], text.getUnicode());
                            } else {
                                if (indexitem[0] >= itemTitle.size()) {
                                    itemTitle.add(indexitem[0], text.getUnicode());
                                } else {
                                    itemTitle.set(indexitem[0], itemTitle.get(indexitem[0]) + text.getUnicode());
                                }
                            }
                        } else if (text.getHeight() > 5.4) { // description de l'item
                            if (text.getUnicode().equals("•")) {
                                //description.get(indexitem) += "--";
                                //System.out.println("[test]Point ? " + (String) parsedText.toString() + (String)text.getUnicode());
                            }

                            // On ajoute le texte à la description et on incremente l'array si besoin
                            if (description.isEmpty()) {
                                description.add(indexitem[0], text.getUnicode());
                            } else {
                                if (indexitem[0] >= description.size()) {
                                    description.add(indexitem[0], text.getUnicode());
                                } else {
                                    description.set(indexitem[0], description.get(indexitem[0]) + text.getUnicode());
                                }
                            }
                        } else if (text.getHeight() > 5.0 && text.getHeight() < 5.4) { // On récupère le tableau de reference
                            /*System.out.print("[test]X : " + textX + " lastX : " + lastTextX[0] + " taille : " + text.getWidth());
                            System.out.print(" -- Nb colonne : " + indexTableCol[0] + " Nb ligne : " + indexTableRow[0]);
                            System.out.println(" -- " + text.getUnicode());*/

                            // On incrmente l'array de description au cas ou l'item n'a pas de description pour maintenir la cohérence des index
                            if (indexitem[0] >= description.size()) {
                                description.add(indexitem[0], "");
                            }

                            if (textX < lastTextX[0]) {
                                // On repasse à la première colonne
                                indexTableCol[0] = 0;
                            }

                            // Incrémentation du numéro de colonne si nécessaire
                            if (textX > lastTextX[0] + (text.getWidth() * 4)) {
                                indexTableCol[0]++;
                            }

                            // Incrémentation du numéro de ligne si nécessaire
                            if (textY > lastTextY[0] + (text.getHeight() * 2)) {
                                indexTableRow[0]++;
                            }

                            // On met à jour le nombre de colonnes et de lignes
                            if (indexTableCol[0] >= nbCol.get(indexitem[0])) {
                                nbCol.set(indexitem[0], indexTableCol[0]);
                            }

                            if (indexTableRow[0] > nbRow.get(indexitem[0])) {
                                nbRow.set(indexitem[0], indexTableRow[0]);
                            }

                            // On ajoute un nouveau tableau si l'arry est vide ou si on est passé à la colonne suivante sinon on rempli le tableau courant
                            if (allTable.isEmpty() || indexitem[0] >= allTable.size()) {
                                String[][] table = new String[10][30]; // Mettre dynamiquement la taille

                                // Boucle pour initialiser chaque élément avec une chaîne vide
                                for (int i = 0; i < table.length; i++) {
                                    for (int j = 0; j < table[i].length; j++) {
                                        table[i][j] = "";
                                    }
                                }

                                allTable.add(table);
                            }

                            // Ajout du texte à la case courante du tableau
                            String[][] currentTable = allTable.get(indexitem[0]);
                            currentTable[indexTableCol[0]][indexTableRow[0]] += text.getUnicode();

                            // Mise à jour des dernières positions
                            lastTextX[0] = textX;
                            lastTextY[0] = textY;

                            //System.out.println(" -- Tableau : " + tableau.get(indexitem[0]).get(0).get(0));
                            //System.out.println(" -- Tableau : " + allTable.get(indexitem[0])[indexTableCol[0]][indexTableRow[0]]);
                        }

                        lastheight[0] = text.getHeight();
                    }
                };
                pdfStripper.setSortByPosition(true);  // Optionnel : trier le texte par position sur la page
                for (int j = 0; j < document.getNumberOfPages(); j++) {
                    pdfStripper.setStartPage(j + 1);
                    pdfStripper.setEndPage(j + 1);
                    pdfStripper.getText(document);
                    images.addAll(getImagesFromResources(pdfStripper.getCurrentPage().getResources(), 60.0f, 800.0f));

                    // On réinitialise l'index de la colonne et de la ligne
                    indexTableCol[0] = 0;
                }
            } catch (IOException e) {
                Log.e("[test]PdfBox-Android-Sample", "Exception thrown while stripping text", e);
            }  /*catch (IndexOutOfBoundsException e) {
                // Gérer l'exception ici
                e.printStackTrace(); // Imprimez la trace de la pile pour vous aider à identifier la cause
                return items;
            }*/ finally {
                try {
                    if (document != null) document.close();
                } catch (IOException e) {
                    Log.e("[test]PdfBox-Android-Sample", "Exception thrown while closing document", e);
                }
            }

            //System.out.println("[test]Nombre d'images extraites : " + images.size());

            // On crée les items
            //System.out.println("[test]Nombre d'items : " + itemTitle.size() + " " + description.size() + " " + table.size() + " " + images.size());
            for (int k = 0; k < itemTitle.size(); k++) {
                //String finalText = parsedText.toString().replaceAll("--", "\n").toString();
                //System.out.println("[test] titres items : " + itemTitle.get(k));
                //System.out.println("[test]descritpion du pdf : " + description.get(k));
                //System.out.println("[test]tableau de reference : " + table.get(k));
                //System.out.println("[test]Colonnes : " + tableHeader);
                //System.out.println("[test]Premiere case : " + allTable.get(k)[0][0]);
                //System.out.println("[test]Deuxième case : " + allTable.get(k)[1][0]);


                // On copie le tableau de référence dans un tableau de String[][] ayant la bonne taille
                int startRow = getStartIndexRowFromTab(allTable.get(k));

                String[][] tableTmp = new String[nbCol.get(k) + 1][nbRow.get(k) + 1 - startRow];
                for (int i = 0; i < nbCol.get(k) + 1; i++) {
                    for (int j = 0; j < tableTmp[i].length; j++) {
                        tableTmp[i][j] = allTable.get(k)[i][j + startRow];
                    }
                }

                Item item = null;

                try {
                    // On crée un nouvel item avec son nom et le lien de son pdf
                    item = new Item(itemTitle.get(k), description.get(k), tableTmp, images.get(k), context);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return items;
                }

                // On ajoute les items à la sous catégorie
                items.add(item);
            }
        }

        return items;
    }

    // Fonction pour ouvrir le PDF avec une application spécifique
    private void openPdfWithApp(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Gérer l'exception si aucune application capable de lire les PDF n'est trouvée
            //Toast.makeText(context, "Aucune application pour ouvrir les PDF n'est installée.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getStartIndexRowFromTab(String[][] tab){
        int startRow = 0;
        boolean empty = false;

        for(int i = 0; i < tab.length; i++){
            for(int j = 0; j < tab[i].length; j++){
                empty = tab[i][j].equals("");

                // Si la case actuelle et la suivante sont vides, on retourne 0
                if (empty && tab[i][j + 1].equals("")) {
                    return startRow;
                } else if (empty) { // Sinon on retourne la ligne suivante
                    return j + 1;
                }
            }
        }

        return startRow;
    }

    private List<Bitmap> getImagesFromResources(PDResources resources, float upperLimit, float lowerLimit) throws IOException {
        List<Bitmap> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources(), upperLimit, lowerLimit));
            } else if (xObject instanceof PDImageXObject) {
                PDImageXObject imageXObject = (PDImageXObject) xObject;
                float imageY = imageXObject.getImage().getHeight();  // Modifier selon votre besoin

                // Filtrer les images en fonction de leur position sur la page
                if (imageY >= upperLimit && imageY <= lowerLimit) {
                    images.add(imageXObject.getImage());
                }
            }
        }

        return images;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getCategoryId(){
        return id;
    }
}

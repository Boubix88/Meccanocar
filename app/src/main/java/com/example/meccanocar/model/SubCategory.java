package com.example.meccanocar.model;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

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
            try {
                if (!file.exists()) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL + this.link));
                    request.setTitle(this.name);
                    request.setDescription("Downloading PDF");

                    // Définir le dossier de destination pour le téléchargement
                    request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, this.name + ".pdf");

                    // Obtenir le gestionnaire de téléchargement du système
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                    // Soumettre la demande de téléchargement au gestionnaire de téléchargement
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                    }

                    file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), this.name + ".pdf");
                }
            } catch (Exception e) {
                Log.e("[test]", "Exception thrown while stripping text", e);
            }

            PDFBoxResourceLoader.init(context);

            ArrayList<String> itemTitle = new ArrayList<>();
            ArrayList<String> description = new ArrayList<>();
            //ArrayList<String> table = new ArrayList<>();
            ArrayList<Bitmap> images = new ArrayList<>();

            //ArrayList<ArrayList<String>> tableHeader = new ArrayList<>();
            final String[][][] table = {new String[0][0]};
            ArrayList<String[][]> allTable = new ArrayList<>();

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
                                //tableHeader.removeAll(tableHeader); // on vide le tableau de reference
                                // On reinitialise tableaHeader
                                table[0] = new String[0][0];
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
                        } else { // description de l'item
                            if (text.getUnicode().equals("•")) {
                                //description.get(indexitem) += "--";
                                //System.out.println("[test]Point ? " + (String) parsedText.toString() + (String)text.getUnicode());
                            }

                            if (description.isEmpty()) {
                                description.add(indexitem[0], text.getUnicode());
                            } else {
                                if (indexitem[0] >= description.size()) {
                                    description.add(indexitem[0], text.getUnicode());
                                } else {
                                    description.set(indexitem[0], description.get(indexitem[0]) + text.getUnicode());
                                }
                            }
                        }

                        // On récupère le tableau de reference
                        if (text.getHeight() > 5.0 && text.getHeight() < 5.4) {
                            /*if (table.isEmpty()) {
                                table.add(text.getUnicode());
                            } else {
                                if (indexitem[0] >= table.size()) {
                                    //tableau a revoir pas tout le temps le meme
                                    //System.out.println("[test]indexitem : " + indexitem[0] + " list : " + table.toString());
                                    table.add(indexitem[0], text.getUnicode());
                                } else {
                                    table.set(indexitem[0], table.get(indexitem[0]) + text.getUnicode());
                                }
                            }*/

                            // Première ligne du tableau
                            //if (text.getFont().getName().contains("Bold")) {
                                // Supposons que vous ayez une référence à votre ArrayList<ArrayList<String>> tableHeader
                                //System.out.println("[test]Tableau : " + tableHeader);
                                // Affichage du tableau
                                /*for (ArrayList<String> column : tableHeader) {
                                    for (String cell : column) {
                                        for (int i = 0; i < cell.length(); i++) {
                                            System.out.print(cell.charAt(i));
                                        }
                                        System.out.print("\t"); // Vous pouvez remplacer System.out.print par la méthode que vous utilisez pour afficher les éléments
                                    }
                                    System.out.println(); // Nouvelle ligne après chaque colonne
                                }*/
                                //System.out.println("[test] Nb colonnes : " + tableHeader.size() + " Nb lignes : " + tableHeader.get(0).size());


                                // On ajoute une nouvelle colonne si l'arry est vide ou si on est passé à la colonne suivante
                                /*if (tableHeader.isEmpty() || indexTableCol[0] >= tableHeader.size()) {
                                    /*ArrayList<String> tmp = new ArrayList<>();
                                    tmp.add(text.getUnicode());
                                    tableHeader.add(indexTableCol[0], tmp);*/
                                    /*tableHeader.add(indexTableCol[0], new ArrayList<>());
                                }*/

                                // On ajoute le texte a la colonne courante

                                // On verifie si la ligne existe
                                /*if (tableHeader.get(indexTableCol[0]) != null && indexTableRow[0] >= tableHeader.get(indexTableCol[0]).size()) {
                                    ArrayList<String> tmp = new ArrayList<>();
                                    tmp.add(text.getUnicode());
                                    tableHeader.get(indexTableCol[0]).add(tmp);
                                } else {
                                    String tmp = tableHeader.get(indexTableCol[0]).get(indexTableRow[0]);
                                    tableHeader.get(indexTableCol[0]).set(indexTableRow[0], tmp + text.getUnicode());
                                }*/
                            //}


                            /*if (textX < lastTextX[0]) {
                                // On repasse à la première colonne
                                indexTableCol[0] = 0;
                            }

                            if (textX + 20 > lastTextX[0]) {
                                // On passe à la colonne suivante
                                indexTableCol[0]++;
                            }

                            if (textY + 6 > lastTextY[0]) {
                                // On passe à la ligne suivante
                                indexTableRow[0]++;
                            }

                            lastTextX[0] = textX;
                            lastTextY[0] = textY;*/

                            //System.out.println("[test]Tableau : " + tableHeader);
                            /*for (ArrayList<String> column : tableHeader) {
                                for (String cell : column) {
                                    System.out.print(cell + "\t");
                                }
                                System.out.println();
                            }*/

                            System.out.print("[test]X : " + textX + " lastX : " + lastTextX[0] + " taille : " + text.getWidth());
                            System.out.print(" -- Nb colonne : " + indexTableCol[0] + " Nb ligne : " + indexTableRow[0]);
                            System.out.print(" -- " + text.getUnicode());

                            /*if (tableHeader.isEmpty()) {
                                lastTextX[0] = textX - text.getWidth();
                                lastTextY[0] = textY - text.getHeight();
                            }*/

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

                            // Dans la case d'index [indexTableCol][indexTableRow] on ajoute le carattere text.uniCode au mot deja present dans la case si'il y en a un sinon on ajouter le caractere et augmentant dynamiquement la taille du tableau de strin
                            // On ajoute une nouvelle colonne si l'arry est vide ou si on est passé à la colonne suivante
                            // Ajout d'une nouvelle colonne si nécessaire
                            if (allTable.isEmpty() || indexitem[0] >= allTable.size()) {
                                String[][] table = new String[50][50];

                                // Boucle pour initialiser chaque élément avec une chaîne vide
                                for (int i = 0; i < table.length; i++) {
                                    for (int j = 0; j < table[i].length; j++) {
                                        table[i][j] = "";
                                    }
                                }

                                allTable.add(table);
                            } else {
                                // Réinitialisation du tableau actuel si on passe à la colonne suivante
                                //allTable.get(indexitem[0]) = new String[indexTableCol[0]][indexTableRow[0]];
                            }

                            // Ajout du texte à la case courante du tableau
                            String[][] currentTable = allTable.get(indexitem[0]);
                            currentTable[indexTableCol[0]][indexTableRow[0]] += text.getUnicode();

                            // Ajout d'une nouvelle colonne si nécessaire
                            /*if (tableHeader[0].isEmpty() || indexTableCol[0] >= tableHeader[0].size()) {
                                tableHeader[0].add(new ArrayList<>());
                            } else {
                                tableHeader[0].add(indexTableCol[0], new ArrayList<>());
                            }*/

                            // Ajout du texte à la colonne courante
                            //ArrayList<String> currentColumn = tableHeader[0].get(indexTableCol[0]);
                            //System.out.println(" -- " + currentColumn.size() + " " + indexTableRow[0]);
                            /*if (indexTableRow[0] > currentColumn.size()) {
                                currentColumn.add(text.getUnicode());
                            } else {
                                String tmp = currentColumn.get(indexTableRow[0]);
                                currentColumn.set(indexTableRow[0], tmp + text.getUnicode());
                                System.out.println("[test]Ajout du texte à la colonne courante : " + tmp + " " + text.getUnicode());
                            }*/

                            // On ajoute a tableau
                            /*if (tableau.isEmpty() || indexitem[0] >= tableau.size()) {
                                tableau.add(indexitem[0], tableHeader[0]);
                            } else {
                                tableau.set(indexitem[0], tableHeader[0]);
                            }*/

                            // Mise à jour des dernières positions
                            lastTextX[0] = textX;
                            lastTextY[0] = textY;

                            //System.out.println(" -- Tableau : " + tableau.get(indexitem[0]).get(0).get(0));
                            System.out.println(" -- Tableau : " + allTable.get(indexitem[0])[indexTableCol[0]][indexTableRow[0]]);
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

                    indexTableCol[0] = 0;
                    indexTableRow[0] = 0;
                }
            } catch (IOException e) {
                Log.e("[test]PdfBox-Android-Sample", "Exception thrown while stripping text", e);
            } finally {
                try {
                    if (document != null) document.close();
                } catch (IOException e) {
                    Log.e("[test]PdfBox-Android-Sample", "Exception thrown while closing document", e);
                }
            }

            System.out.println("[test]Nombre d'images extraites : " + images.size());

            // On crée les items
            //System.out.println("[test]Nombre d'items : " + itemTitle.size() + " " + description.size() + " " + table.size() + " " + images.size());
            for (int k = 0; k < itemTitle.size(); k++) {
                //String finalText = parsedText.toString().replaceAll("--", "\n").toString();
                System.out.println("[test] titres items : " + itemTitle.get(k));
                System.out.println("[test]descritpion du pdf : " + description.get(k));
                //System.out.println("[test]tableau de reference : " + table.get(k));
                //System.out.println("[test]Colonnes : " + tableHeader);
                /*System.out.println("[test]Premiere case : " + allTable.get(k).get(0).get(0));
                System.out.println("[test]Deuxième case : " + allTable.get(k).get(1).get(0));*/

                // On crée un nouvel item avec son nom et le lien de son pdf
                Item item = new Item(itemTitle.get(k), description.get(k), "", images.get(k), context); // modifier le ""

                // On ajoute les items à la sous catégorie
                items.add(item);
            }
        }

        return items;
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

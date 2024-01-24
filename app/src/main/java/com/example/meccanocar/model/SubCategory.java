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

            ArrayList<String> itemTitle = new ArrayList<>(50);
            ArrayList<String> description = new ArrayList<>(50);
            ArrayList<String> table = new ArrayList<>(50);
            ArrayList<Bitmap> images = new ArrayList<>();
            final int[] indexitem = {-1};
            final float[] lastheight = {0.0f};


            PDDocument document = null;
            PdfRenderer pdfRenderer = null;
            try {
                pdfRenderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
                document = PDDocument.load(file);
            } catch (IOException e) {
                Log.e("[test]", "Exception thrown while loading document to strip", e);
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
                                        /*if (text.getHeight() > 5.0 && text.getHeight() < 5.4) {
                                            if (table.isEmpty()) {
                                                table.add(text.getUnicode());
                                            } else {
                                                if (indexitem[0] >= table.size()) {
                                                    //tableau a revoir pas tout le temps le meme
                                                    //System.out.println("[test]indexitem : " + indexitem[0] + " list : " + table.toString() + " nom pdf : " + itemName);
                                                    table.add(indexitem[0], text.getUnicode());
                                                } else {
                                                    table.set(indexitem[0], table.get(indexitem[0]) + text.getUnicode());
                                                }
                                            }
                                        }*/

                        lastheight[0] = text.getHeight();
                    }
                };
                pdfStripper.setSortByPosition(true);  // Optionnel : trier le texte par position sur la page
                for (int j = 0; j < document.getNumberOfPages(); j++) {
                    pdfStripper.setStartPage(j + 1);
                    pdfStripper.setEndPage(j + 1);
                                    /*parsedText.append(pdfStripper.getText(document));
                                    parsedText.append(pdfStripper.getLineSeparator());*/
                    pdfStripper.getText(document);

                    // Récupérer l'image de la page
                                    /*PdfRenderer.Page page = pdfRenderer.openPage(j);
                                    Bitmap image = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                                    page.render(image, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                                    page.close();*/

                    images.addAll(getImagesFromResources(pdfStripper.getCurrentPage().getResources()));

                    // Ajouter l'image à la liste
                    //images.add(image);
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
            System.out.println("[test]Nombre d'items : " + itemTitle.size() + " " + description.size() + " " + table.size() + " " + images.size());
            for (int k = 0; k < itemTitle.size(); k++) {
                //String finalText = parsedText.toString().replaceAll("--", "\n").toString();
                System.out.println("[test] titres items : " + itemTitle.get(k));
                System.out.println("[test]descritpion du pdf : " + description.get(k));
                //System.out.println("[test]tableau de reference : " + table.get(0));

                // On crée un nouvel item avec son nom et le lien de son pdf
                Item item = new Item(itemTitle.get(k), description.get(k), "", images.get(k)); // modifier le ""

                // On ajoute les items à la sous catégorie
                items.add(item);
            }
        }

        return items;
    }

    private List<Bitmap> getImagesFromResources(PDResources resources) throws IOException {
        List<Bitmap> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
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

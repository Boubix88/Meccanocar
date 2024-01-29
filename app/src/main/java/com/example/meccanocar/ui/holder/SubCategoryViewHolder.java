package com.example.meccanocar.ui.holder;

// SubCategoryViewHolder.java
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.MainActivity;
import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.SubCategory;
import com.example.meccanocar.model.manager.MeccanocarManager;
import com.example.meccanocar.ui.adapter.ItemAdapter;
import com.example.meccanocar.ui.catalog.ItemsDetailsActivity;
import com.example.meccanocar.ui.home.NewsDetailsActivity;

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

        // on recupere les 3 logos et le text chargement
        ImageView logoLeftImageView = root.findViewById(R.id.catalog_logo_left);
        ImageView logoMiddleImageView = root.findViewById(R.id.catalog_logo_middle);
        ImageView logoRightImageView = root.findViewById(R.id.catalog_logo_right);
        TextView loadingTextView = root.findViewById(R.id.textViewLoadingItems);

        // On crée les 3 animations
        ObjectAnimator rotationAnimator1 = (ObjectAnimator) AnimatorInflater.loadAnimator(root.getContext(), R.animator.rotation_logo_left);
        ObjectAnimator rotationAnimator2 = (ObjectAnimator) AnimatorInflater.loadAnimator(root.getContext(), R.animator.rotation_logo_middle);
        ObjectAnimator rotationAnimator3 = (ObjectAnimator) AnimatorInflater.loadAnimator(root.getContext(), R.animator.rotation_logo_right);

        // On associe les animations aux logos
        rotationAnimator1.setTarget(logoLeftImageView);
        rotationAnimator2.setTarget(logoMiddleImageView);
        rotationAnimator3.setTarget(logoRightImageView);

        // Créez un AnimatorSet pour les exécuter séquentiellement
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotationAnimator1).before(rotationAnimator2);
        animatorSet.play(rotationAnimator2).before(rotationAnimator3);

        // Configurez un écouteur pour redémarrer l'ensemble à la fin
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start(); // Redémarrez l'ensemble
            }
        });

        // Ajout du listener sur chaque item
        subCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On affiche les 3 logos et on lance l'animation
                logoLeftImageView.setVisibility(View.VISIBLE);
                logoMiddleImageView.setVisibility(View.VISIBLE);
                logoRightImageView.setVisibility(View.VISIBLE);
                loadingTextView.setVisibility(View.VISIBLE);
                animatorSet.start();

                // On masque la recyclerView des categories
                recyclerView.setVisibility(View.GONE);

                // Récupérer l'objet SubCategory associé à cet élément de la liste
                // TEST à retirer plus tard et modifier pour afficher les items
                SubCategory subCategory = getSubCategory();

                // Créer un AsyncTask pour charger les items en arrière-plan
                new AsyncTask<Void, Void, ArrayList<Item>>() {
                    // Méthode appelée avant le traitement de chargement
                    @Override
                    protected ArrayList<Item> doInBackground(Void... voids) {
                        // Effectuez votre traitement de chargement ici
                        return subCategory.getItems();
                    }

                    // Méthode appelée lorsque le traitement de chargement est terminé
                    @Override
                    protected void onPostExecute(ArrayList<Item> items) {
                        // On cache les 3 logos et on arrete l'animation
                        logoLeftImageView.setVisibility(View.GONE);
                        logoMiddleImageView.setVisibility(View.GONE);
                        logoRightImageView.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        animatorSet.cancel();

                        // Si c'est une catgegorie sans items on l'affiche en telechargeant le pdf sinon on affiche les items
                        if (items.isEmpty()) {
                            // Vérifier si l'objet SubCategory et le lien PDF ne sont pas null
                            if (subCategory != null && subCategory.getLink() != null) {
                                // Ouvrir le PDF avec une application externe
                                openPdfWithExternalApp(itemView.getContext(), subCategory.getLink());

                                // On met a jour la liste des 5 derniers subCategories vus
                                MeccanocarManager.getInstance().updateLast5ItemsViewed(subCategory);

                                // On affiche la recyclerView des categories
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(itemView.getContext(), R.string.open_pdf_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            // Créer un adaptateur personnalisé (vous devrez le créer)
                            ItemAdapter adapter = new ItemAdapter(items);

                            // Définir l'adaptateur pour la GridView
                            gridView.setAdapter(adapter);

                            // Votre code après le chargement des items
                            gridView.setVisibility(View.VISIBLE); // Ajout de cette ligne

                            // Définissez le OnClickListener pour chaque élément de la GridView
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Obtenez l'élément sélectionné
                                    Item selectedItem = (Item) parent.getItemAtPosition(position);
                                    System.out.println("[holder]Titre de l'item : " + selectedItem.getName());
                                    System.out.println("[holder]Description de l'item : " + selectedItem.getDescription());

                                    // On affiche l'item en particulier
                                    Context context = itemView.getContext();
                                    Intent intent = new Intent(context, ItemsDetailsActivity.class);
                                    intent.putExtra("item", selectedItem); // On passe l'objet item à l'activité

                                    context.startActivity(intent);
                                }
                            });
                        }
                    }
                }.execute();
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

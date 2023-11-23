package com.example.meccanocar.model.manager;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

import com.example.meccanocar.MainActivity;
import com.example.meccanocar.R;
import com.example.meccanocar.model.Meccanocar;
import com.example.meccanocar.model.listener.CatalogLoadListener;
import com.example.meccanocar.model.listener.NewsLoadListener;

public class TaskManager {
    private int tasksCount = 0;
    private Context context;

    public TaskManager(Context context) {
        this.context = context;
    }

    public void startTasks() {
        tasksCount = 2; // On exécute deux tâches (catalogue et news)

        // On instancie le model
        Meccanocar meccanocar = new Meccanocar(context);

        // On enregistre l'instance du model
        MeccanocarManager.setInstance(meccanocar);

        // Chargement des données depuis le stockage local
        meccanocar.loadLast5ItemsViewedFromJson();

        // Chargement des données depuis le site web (news)
        meccanocar.getNewsFromHttp(new NewsLoadListener() {
            @Override
            public void onNewsLoaded() {
                onTaskCompleted();
            }
        });

        // Chargement des données depuis le site web (catalogue)
        meccanocar.getCatalog().getCatalogFromHttp(new CatalogLoadListener() {
            @Override
            public void onCatalogLoaded() {
                onTaskCompleted();
            }
        });
    }

    private synchronized void onTaskCompleted() {
        tasksCount--;
        if (tasksCount == 0) {
            launchMainActivity();
        }
    }

    private void launchMainActivity() {
        // Création de l'animation de fondu sortant
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out);

        // On lance l'action principale avec l'animation
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent, options.toBundle());

        // Terminer l'activité actuelle si nécessaire
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
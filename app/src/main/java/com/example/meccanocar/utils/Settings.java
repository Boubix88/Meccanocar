package com.example.meccanocar.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private String language;
    private String theme;
    private static Context context;

    public Settings(String language, String theme, Context context) {
        this.language = language;
        this.theme = theme;
        this.context = context;
    }

    public String getLanguage() {
        return language;
    }

    public String getTheme() {
        return theme;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    // Méthode pour sauvegarder les paramètres
    public void saveSettings() {
        File path = context.getExternalFilesDir("");
        if (path != null) {
            File file = new File(path, "settings.json");

            // Convertir l'objet Settings en format JSON
            Gson gson = new Gson();
            String json = gson.toJson(this);

            // Écrire le JSON dans le fichier
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(json);
                fileWriter.flush(); // Assurez-vous que les données sont bien écrites
                fileWriter.close(); // Fermez le FileWriter
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour charger les paramètres
    public static Settings loadSettings() {
        // Charger les paramètres
        File path = context.getExternalFilesDir("");
        if (path != null) {
            File file = new File(path, "settings.json");
            if (file.exists()) {
               StringBuilder stringBuilder = new StringBuilder();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Convertir le JSON en objet Settings
                Gson gson = new Gson();
                return gson.fromJson(stringBuilder.toString(), Settings.class);
            }
        }

        return new Settings("auto", "auto", context);
    }
}

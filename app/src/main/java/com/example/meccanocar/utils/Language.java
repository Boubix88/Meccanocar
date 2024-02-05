package com.example.meccanocar.utils;

import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.meccanocar.model.manager.MeccanocarManager;

import java.util.Locale;

public class Language {
    public static void setLanguage(Resources resources) {
        Settings settings = MeccanocarManager.getInstance().getSettings();
        String language = settings.getLanguage();

        switch (language) {
            case "auto":
                Configuration sysConfig = Resources.getSystem().getConfiguration();
                String systemLanguage = sysConfig.locale.getLanguage();

                setLocale(systemLanguage, resources);
                break;
            case "fr":
                setLocale("fr", resources);
                break;
            case "en":
                setLocale("en", resources);
                break;
        }
    }

    public static void setLocale(String languageCode, Resources resources) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}

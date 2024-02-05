package com.example.meccanocar;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import com.example.meccanocar.utils.Language;
import com.example.meccanocar.model.manager.MeccanocarManager;
import com.example.meccanocar.ui.catalog.CatalogViewModel;
import com.example.meccanocar.ui.home.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.AnimRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.meccanocar.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int NAVIGATION_HOME = R.id.navigation_home;
    private static final int NAVIGATION_DASHBOARD = R.id.navigation_dashboard;
    private static final int NAVIGATION_NOTIFICATIONS = R.id.navigation_notifications;

    public static File EXTERNAL_STORAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On set la langue de l'application
        Language.setLanguage(getResources());

        EXTERNAL_STORAGE_PATH = this.getExternalFilesDir("");

        // Vérification de la version d'Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Vérification si l'application a déjà l'autorisation
            if (!Environment.isExternalStorageManager()) {
                // Si l'application n'a pas l'autorisation, demandez à l'utilisateur
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);


            if (itemId == R.id.navigation_home) {
                //navController.navigate(R.id.navigation_home, null, getNavOptions(R.anim.enter_right_to_left, R.anim.exit_left_to_right));
                navController.navigate(R.id.navigation_home, null, getNavOptions(R.anim.fade_in, R.anim.fade_out));
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                //navController.navigate(R.id.navigation_dashboard, null, getNavOptions(R.anim.enter_left_to_right, R.anim.exit_right_to_left));
                navController.navigate(R.id.navigation_dashboard, null, getNavOptions(R.anim.fade_in, R.anim.fade_out));
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                //navController.navigate(R.id.navigation_notifications, null, getNavOptions(R.anim.enter_left_to_right, R.anim.exit_right_to_left));
                navController.navigate(R.id.navigation_notifications, null, getNavOptions(R.anim.fade_in, R.anim.fade_out));
                return true;
            }

            return false;
        });

        // Obtenir une référence au ViewModel partagé
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Mettre à jour les données dans le ViewModel
        homeViewModel.setMeccanocarData(MeccanocarManager.getInstance());

        // Obtenir une référence au ViewModel partagé
        CatalogViewModel catalogViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        // Mettre à jour les données dans le ViewModel
        catalogViewModel.setMeccanocarData(MeccanocarManager.getInstance());

        getSupportActionBar().hide(); // Masquer l'ActionBar
    }

    @Override
    public Resources.Theme getTheme() {
        com.example.meccanocar.utils.Settings settings = MeccanocarManager.getInstance().getSettings();
        Resources.Theme theme = super.getTheme();

        switch (settings.getTheme()) {
            case "light":
                theme.applyStyle(R.style.Theme_MeccanocarLight, true);
                break;
            case "dark":
                theme.applyStyle(R.style.Theme_MeccanocarDark, true);
                break;
            case "auto":
                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        theme.applyStyle(R.style.Theme_MeccanocarDark, true);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        theme.applyStyle(R.style.Theme_MeccanocarLight, true);
                        break;
                }
                break;
        }

        return super.getTheme();
    }

    private NavOptions getNavOptions(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        return new NavOptions.Builder()
                .setEnterAnim(enterAnim)
                .setExitAnim(exitAnim)
                .build();
    }
}
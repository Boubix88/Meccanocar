package com.example.meccanocar;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import com.example.meccanocar.model.Meccanocar;
import com.example.meccanocar.model.MeccanocarManager;
import com.example.meccanocar.ui.catalog.CatalogViewModel;
import com.example.meccanocar.ui.home.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.meccanocar.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // On instancie le model
        Meccanocar meccanocar = new Meccanocar(this);
        MeccanocarManager.setInstance(meccanocar);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Obtenir une référence au ViewModel partagé
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Mettre à jour les données dans le ViewModel
        homeViewModel.setMeccanocarData(meccanocar);

        // Obtenir une référence au ViewModel partagé
        CatalogViewModel catalogViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        // Mettre à jour les données dans le ViewModel
        catalogViewModel.setMeccanocarData(meccanocar);

        getSupportActionBar().hide(); // Masquer l'ActionBar
    }
}
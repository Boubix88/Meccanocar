package com.example.meccanocar;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meccanocar.model.manager.TaskManager;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Masquer la barre de statut
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // On démarre les tâches et une fois terminées, on lance l'activité principale
        TaskManager taskManager = new TaskManager(this);
        taskManager.startTasks();
    }
}
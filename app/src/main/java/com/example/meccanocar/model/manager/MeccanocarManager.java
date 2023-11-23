package com.example.meccanocar.model.manager;

import com.example.meccanocar.model.Meccanocar;

public class MeccanocarManager {
    private static Meccanocar instance;

    public static void setInstance(Meccanocar meccanocarInstance) {
        instance = meccanocarInstance;
    }

    public static Meccanocar getInstance() {
        return instance;
    }
}
package com.example.meccanocar.model;

public class MeccanocarManager {
    private static Meccanocar instance;

    public static void setInstance(Meccanocar meccanocarInstance) {
        instance = meccanocarInstance;
    }

    public static Meccanocar getInstance() {
        return instance;
    }
}
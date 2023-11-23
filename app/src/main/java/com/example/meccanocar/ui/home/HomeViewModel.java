package com.example.meccanocar.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.Meccanocar;
import com.example.meccanocar.model.MeccanocarManager;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Meccanocar> meccanocarData;
    private Meccanocar meccanocar;

    public HomeViewModel() {
        meccanocarData = new MutableLiveData<>();
        meccanocar = MeccanocarManager.getInstance();

        loadMeccanocar(); // Chargez vos données Meccanocar ici
    }

    public ArrayList<Item> getLast5ItemsViewed(){
        return this.meccanocar.getLast5ItemsViewed();
    }

    private void loadMeccanocar() {
        // Supposons que vous avez une logique pour charger vos données Meccanocar
        meccanocarData.setValue(meccanocar);
    }

    public void setMeccanocarData(Meccanocar m) {
        meccanocar = m;
    }

    public Meccanocar getMecanocar() {
        return meccanocar;
    }
}
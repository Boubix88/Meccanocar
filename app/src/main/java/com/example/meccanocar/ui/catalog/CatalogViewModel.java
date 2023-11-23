package com.example.meccanocar.ui.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meccanocar.model.Category;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.Meccanocar;
import com.example.meccanocar.model.manager.MeccanocarManager;

import java.util.ArrayList;
import java.util.List;

public class CatalogViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Meccanocar> meccanocarData;
    private final MutableLiveData<ArrayList<Category>> categoriesLiveData;
    private final MutableLiveData<ArrayList<Item>> itemsLiveData;
    private Meccanocar meccanocar;

    public CatalogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");

        meccanocarData = new MutableLiveData<>();
        categoriesLiveData = new MutableLiveData<>();
        itemsLiveData = new MutableLiveData<>();

        meccanocar = MeccanocarManager.getInstance();

        loadMeccanocar(); // Chargez vos données Meccanocar ici
    }

    public LiveData<String> getText() {
        return mText;
    }

    private void loadMeccanocar() {
        // Supposons que vous avez une logique pour charger vos données Meccanocar
        meccanocarData.setValue(meccanocar);

        // Chargez les catégories
        loadCategories();
    }

    private void loadCategories() {
        Meccanocar meccanocar = meccanocarData.getValue();
        if (meccanocar != null) {
            ArrayList<Category> categories = meccanocar.getCatalog().getCatalog();
            categoriesLiveData.setValue(categories);
        }
    }

    public List<Item> getAllItems(){
        return this.meccanocar.getCatalog().getAllItems();
    }

    public LiveData<ArrayList<Category>> getCategories() {
        return categoriesLiveData;
    }

    public LiveData<ArrayList<Item>> getItems() {
        return itemsLiveData;
    }

    public void setSelectedCategory(String selectedCategoryName) {
        Meccanocar meccanocar = meccanocarData.getValue();
        if (meccanocar != null) {
            // Utilisez une logique pour obtenir la catégorie correspondante en fonction de son nom
            Category selectedCategory = meccanocar.getCatalog().getCategoryByName(selectedCategoryName);
            itemsLiveData.setValue(selectedCategory != null ? selectedCategory.getItems() : new ArrayList<>());
        }
    }

    public void setMeccanocarData(Meccanocar m) {
        meccanocar = m;
    }
}
package com.example.meccanocar.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meccanocar.model.Meccanocar;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Meccanocar> meccanocarData;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");

        meccanocarData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Meccanocar> getMeccanocarData() {
        return meccanocarData;
    }

    public void setMeccanocarData(Meccanocar meccanocar) {
        meccanocarData.setValue(meccanocar);
    }

    public void setSelectedCategory(String selectedCategory) {
    }
}
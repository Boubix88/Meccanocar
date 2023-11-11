package com.example.meccanocar.ui.catalog;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter adapter; // l'adaptateur
    private RecyclerView.LayoutManager layoutManager; // le gesdtionnaire de mise en page

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Recycler view
        this.categoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        this.categoryRecyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this.getContext());
        this.categoryRecyclerView.setLayoutManager(layoutManager);

        List<Category> category = dashboardViewModel.getCategories().getValue();
        adapter = new CategoryAdapter(category);
        this.categoryRecyclerView.setAdapter(adapter);

        return root;
    }
}

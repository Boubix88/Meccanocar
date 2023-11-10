package com.example.meccanocar.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.databinding.FragmentDashboardBinding;
import com.example.meccanocar.ui.adapter.ItemAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private Spinner categorySpinner;
    private RecyclerView itemRecyclerView;
    private ItemAdapter itemAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        categorySpinner = root.findViewById(R.id.categorySpinner);
        itemRecyclerView = root.findViewById(R.id.itemRecyclerView);

        // Observer pour les catégories
        dashboardViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            // Mettez à jour le Spinner avec la liste des catégories
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryAdapter);

            // Observer pour les items lorsque la catégorie sélectionnée change
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedCategory = (String) parentView.getItemAtPosition(position);
                    dashboardViewModel.setSelectedCategory(selectedCategory);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Ne rien faire ici
                }
            });
        });

        // Observer pour les items
        dashboardViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            // Mettez à jour la liste des items
            itemAdapter.setItems(items);
        });

        // Configurer le RecyclerView pour les items
        itemAdapter = new ItemAdapter();
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemRecyclerView.setAdapter(itemAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
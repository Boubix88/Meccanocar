package com.example.meccanocar.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.SubCategory;
import com.example.meccanocar.ui.holder.SubCategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

// SubCategoryAdapter.java
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryViewHolder> {
    private List<SubCategory> subCategories;
    private View root;

    public SubCategoryAdapter(List<SubCategory> subCategories, View root) {
        this.subCategories = new ArrayList<>(subCategories);
        this.root = root;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sub_category_card, parent, false);
        return new SubCategoryViewHolder(view, root, subCategories);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategory subCategory = subCategories.get(position);
        holder.afficher(subCategory);
    }

    @Override
    public int getItemCount() {
        if (subCategories != null)
            return subCategories.size();
        return 0;
    }
}
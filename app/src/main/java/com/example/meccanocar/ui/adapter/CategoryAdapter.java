package com.example.meccanocar.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.catalog.CategoryViewHolder;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<Category> category = null;

    public CategoryAdapter(List<Category> contacts){
        if (contacts != null){
            this.category = contacts;
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c= category.get(position);
        holder.afficher(c);
    }

    @Override
    public int getItemCount() {
        if(category != null)
            return category.size();
        return 0;
    }
}

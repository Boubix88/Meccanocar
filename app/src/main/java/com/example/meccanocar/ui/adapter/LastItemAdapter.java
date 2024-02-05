package com.example.meccanocar.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.SubCategory;
import com.example.meccanocar.ui.holder.LastItemViewHolder;
import com.example.meccanocar.ui.holder.SubCategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

// SubCategoryAdapter.java
public class LastItemAdapter extends RecyclerView.Adapter<LastItemViewHolder> {
    private List<Item> items;
    private View root;

    public LastItemAdapter(List<Item> items, View root) {
        this.items = new ArrayList<>(items);
        this.root = root;
    }

    @NonNull
    @Override
    public LastItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new LastItemViewHolder(view, root, items);
    }

    @Override
    public void onBindViewHolder(@NonNull LastItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.afficher(item);
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }
}
package com.example.meccanocar.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Item;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private ArrayList<Item> items;

    public ItemAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
        }

        // Récupérer les vues de votre mise en page grid_item_layout
        ImageView itemImage = convertView.findViewById(R.id.imageItem);
        TextView itemName = convertView.findViewById(R.id.nameItem);
        // Autres vues nécessaires...

        // Récupérer l'élément actuel
        Item currentItem = (Item) getItem(position);

        // Mettre à jour les vues avec les données de l'élément actuel
        itemName.setText(currentItem.getName());
        itemImage.setImageBitmap(currentItem.getBitmap());

        return convertView;
    }
}
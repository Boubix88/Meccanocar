package com.example.meccanocar.ui.catalog;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final ImageView image;
    private Category category;

    public CategoryViewHolder(final View itemView) {
        super(itemView);

        name = ((TextView)itemView.findViewById(R.id.nameCategory));
        image = ((ImageView)itemView.findViewById(R.id.imageCategory));

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle(category.getName())
                        .setMessage(category.toStringItems())
                        .show();
            }
        });
    }

    public void afficher(Category category){
        this.category = category;
        name.setText(category.getName());
        System.out.println("[url]Url : " + category.getUrlImage());
        Picasso.get().load(category.getUrlImage()).into(image);
    }
}

package com.example.meccanocar.ui.catalog;

import android.widget.ImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.meccanocar.ui.adapter.ItemAdapter;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryDetailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_layout);

        // Récupérez la catégorie de l'intent
        // Récupérez la catégorie avec getSerializableExtra
        Category category = (Category) getIntent().getSerializableExtra("category");

        // Affichez les détails de la catégorie
        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);
        ItemAdapter itemAdapter = new ItemAdapter(category.getItems());
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(itemAdapter);

        // Configurez le bouton de retour
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Retournez à l'activité précédente
            }
        });
    }
}

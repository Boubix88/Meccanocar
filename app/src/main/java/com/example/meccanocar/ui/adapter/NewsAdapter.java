package com.example.meccanocar.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.meccanocar.R;
import com.example.meccanocar.model.News;
import com.example.meccanocar.ui.home.NewsDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        // Set image (assuming you have a method getImage() in your News class)
        news.loadImage(holder.imageViewNews);

        // Set date
        holder.dateNews.setText(news.getDate());

        // Set title
        holder.titleNews.setText(news.getTitle());

        // Set recap
        holder.recapNews.setText(news.getRecap());
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNews;
        TextView dateNews;
        TextView titleNews;
        TextView recapNews;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewNews = itemView.findViewById(R.id.imageViewNews);
            dateNews = itemView.findViewById(R.id.dateNews);
            titleNews = itemView.findViewById(R.id.titleNews);
            recapNews = itemView.findViewById(R.id.recapNews);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ouvrir la page de détails lorsque l'élément est cliqué
                    int position = getAdapterPosition();
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("news", newsList.get(position)); // On passe l'objet news à l'activité

                    context.startActivity(intent);
                }
            });
        }
    }

}
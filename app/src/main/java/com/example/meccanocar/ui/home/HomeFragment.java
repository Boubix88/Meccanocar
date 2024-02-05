package com.example.meccanocar.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.meccanocar.R;
import com.example.meccanocar.databinding.FragmentHomeBinding;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.News;
import com.example.meccanocar.model.manager.MeccanocarManager;
import com.example.meccanocar.ui.adapter.LastItemAdapter;
import com.example.meccanocar.ui.adapter.SubCategoryAdapter;
import com.example.meccanocar.ui.adapter.NewsAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private LastItemAdapter lastItemAdapter;
    private TextView textViewNoLastItem;
    private ViewPager2 viewPager;
    private List<News> newsList; // Votre liste de news
    private ImageView imageViewNews;
    private TextView dateNews;
    private TextView titleNews;
    private TextView recapNews;
    private TextView descriptionNews;
    private NewsAdapter newsAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Récupération du TextView
        textViewNoLastItem = root.findViewById(R.id.textViewNoLastItem);
        textViewNoLastItem.setVisibility(View.GONE);

        // Récupération de la RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewHome);

        // Liste des 5 derniers items vus
        List<Item> items = homeViewModel.getLast5ItemsViewed();

        // Création de l'adaptateur et liaison avec la ListView
        lastItemAdapter = new LastItemAdapter(items, root);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // On met l'orientation à HORIZONTAL
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager); // A ajouter sinon rien ne s'affiche
        recyclerView.setAdapter(lastItemAdapter);

        //subCategoryAdapter.notifyDataSetChanged();

        if (items != null && items.size() == 0) {
            textViewNoLastItem.setVisibility(View.VISIBLE);
        } else {
            textViewNoLastItem.setVisibility(View.GONE);
        }


        // Récupération des vues
        imageViewNews = root.findViewById(R.id.imageViewNews);
        dateNews = root.findViewById(R.id.dateNews);
        titleNews = root.findViewById(R.id.titleNews);
        recapNews = root.findViewById(R.id.recapNews);

        // Initialisez votre ViewPager2
        viewPager = root.findViewById(R.id.viewPager);

        // Initialisez votre liste de news
        newsList = homeViewModel.getNews(); // Obtenez vos données de news ici
        System.out.println("[HomeFragment] onViewCreated newsList : " + newsList);

        // Créez un adaptateur pour le ViewPager2
        newsAdapter = new NewsAdapter(newsList);
        viewPager.setAdapter(newsAdapter);

        // ... (le reste de votre code)

        TabLayout tabLayout = root.findViewById(R.id.tabLayout);

        // Associez le TabLayout au ViewPager2
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {
                    // On met à jour le titre de chaque onglet
                    tab.setIcon(R.drawable.point_tablayout);
                }
        );
        tabLayoutMediator.attach();

        // On fait défiler automatiquement les news
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == newsList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 4000); // On change de news toutes les 4 secondes
            }
        };

        // On démarre le défilement automatique après 3 secondes
        handler.postDelayed(update, 4000);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
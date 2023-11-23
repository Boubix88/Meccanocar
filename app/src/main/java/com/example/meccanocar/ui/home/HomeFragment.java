package com.example.meccanocar.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.meccanocar.R;
import com.example.meccanocar.databinding.FragmentHomeBinding;
import com.example.meccanocar.model.Item;
import com.example.meccanocar.model.MeccanocarManager;
import com.example.meccanocar.model.News;
import com.example.meccanocar.ui.adapter.ItemAdapter;
import com.example.meccanocar.ui.adapter.NewsAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private TextView textViewNoLastItem;
    private ViewPager2 viewPager;
    private List<News> newsList; // Votre liste de news
    private ImageView imageViewNews;
    private TextView dateNews;
    private TextView titleNews;
    private TextView recapNews;
    private TextView descriptionNews;


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
        itemAdapter = new ItemAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext())); // A ajouter sinon rien ne s'affiche
        recyclerView.setAdapter(itemAdapter);

        itemAdapter.notifyDataSetChanged();

        if (items != null && items.size() == 0) {
            textViewNoLastItem.setVisibility(View.VISIBLE);
        } else {
            textViewNoLastItem.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Récupération des vues
        imageViewNews = view.findViewById(R.id.imageViewNews);
        dateNews = view.findViewById(R.id.dateNews);
        titleNews = view.findViewById(R.id.titleNews);
        recapNews = view.findViewById(R.id.recapNews);

        // Initialisez votre ViewPager2
        viewPager = view.findViewById(R.id.viewPager);

        // Initialisez votre liste de news
        newsList = MeccanocarManager.getInstance().getNews(); // Obtenez vos données de news ici

        // Créez un adaptateur pour le ViewPager2
        NewsAdapter newsAdapter = new NewsAdapter(newsList);
        viewPager.setAdapter(newsAdapter);

        // ... (le reste de votre code)

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Associez le TabLayout au ViewPager2
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {
                    // Personnalisez ici les indicateurs si besoin
                    tab.setText("News " + (position + 1));
                }
        );
        tabLayoutMediator.attach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.meccanocar.ui.catalog;

import static android.text.TextUtils.replace;

import static androidx.fragment.app.FragmentManagerKt.commit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meccanocar.R;
import com.example.meccanocar.model.Category;
import com.example.meccanocar.ui.adapter.CategoryAdapter;
import com.example.meccanocar.ui.home.HomeFragment;
import com.example.meccanocar.ui.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private CatalogViewModel catalogViewModel;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter adapter; // l'adaptateur
    private RecyclerView.LayoutManager layoutManager; // le gesdtionnaire de mise en page

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        catalogViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_catalog, container, false);

        // Recycler view
        this.categoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        this.categoryRecyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this.getContext());
        this.categoryRecyclerView.setLayoutManager(layoutManager);

        List<Category> category = catalogViewModel.getCategories().getValue();
        adapter = new CategoryAdapter(category, root);
        this.categoryRecyclerView.setAdapter(adapter);

        // Vous pouvez intégrer l'animation lors de la transaction vers ce fragment
        /*if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right, R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            //View view = (View) root.getParent();
            //view.findViewById(R.id.nav_host_fragment_activity_main)
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, new CatalogFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }*/



        /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right, R.anim.enter_left_to_right, R.anim.exit_right_to_left);
        //View view = (View) root.getParent();
        //view.findViewById(R.id.nav_host_fragment_activity_main)
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, new CatalogFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = new HomeFragment();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_left_to_right, R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();*/
    }
}

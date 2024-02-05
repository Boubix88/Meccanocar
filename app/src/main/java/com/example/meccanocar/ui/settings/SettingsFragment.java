package com.example.meccanocar.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.meccanocar.R;
import com.example.meccanocar.databinding.FragmentSettingsBinding;
import com.example.meccanocar.utils.AppInfo;
import com.example.meccanocar.utils.Language;
import com.example.meccanocar.utils.Settings;
import com.example.meccanocar.model.manager.MeccanocarManager;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        Settings settings = MeccanocarManager.getInstance().getSettings();
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // On récupère la version de l'application
        //int versionCode = BuildConfig.VERSION_CODE;
        //String versionName = BuildConfig.VERSION_NAME;
        //String versionCode = AppInfo.getAppVersionCode(getContext());
        String versionCode = "0.349"; // A chaque commit on rajooute le nombre de fichiers modifiés + 0.0 (ex : 0.0 + 53 = 0.053)
        String versionName = AppInfo.getAppVersionName(getContext());

        // On affiche la version de l'application
        TextView versionCodeTxt = root.findViewById(R.id.textViewVersionCode);
        versionCodeTxt.setText(getText(R.string.version_code_text) + " : " + versionCode);

        // On affiche le nom de la version de l'application
        TextView versionNameTxt = root.findViewById(R.id.textViewVersionName);
        versionNameTxt.setText(getText(R.string.version_name_text) + " : " + versionName);

        // Language choice
        String[] languageChoice = new String[] {getResources().getString(R.string.auto_language), "Français", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, languageChoice);

        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.filled_exposed) ;
        autoCompleteTextView.setAdapter(adapter);

        // On met le spinner en fonction de la langue actuelle
        if (settings.getLanguage().equals("auto")) {
            autoCompleteTextView.setText(languageChoice[0], false);
        }
        else if (settings.getLanguage().equals("fr")) {
            autoCompleteTextView.setText(languageChoice[1], false);
        }
        else if (settings.getLanguage().equals("en")) {
            autoCompleteTextView.setText(languageChoice[2], false);
        }

        // On choisit le language
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Settings settings = MeccanocarManager.getInstance().getSettings();
                String language = autoCompleteTextView.getText().toString();
                if (language.equals(languageChoice[0])) {
                    // Langue automatique par rapport a la langue d'android
                    settings.setLanguage("auto");

                    // On set la langue de l'application
                    Language.setLanguage(getResources());
                }
                else if (language.equals(languageChoice[1])) {
                    settings.setLanguage("fr");

                    // On set la langue de l'application
                    Language.setLanguage(getResources());
                }
                else if (language.equals(languageChoice[2])) {
                    settings.setLanguage("en");

                    // On set la langue de l'application
                    Language.setLanguage(getResources());
                }

                // On sauvagarde les paramètres
                settings.saveSettings();

                // Recréez l'activité pour appliquer la nouvelle langue
                Intent intent = new Intent(getContext(), getActivity().getClass());
                startActivity(intent);
                getActivity().finish(); // Fermez l'activité actuelle
            }
        });

        // Theme choice
        String[] themeChoice = new String[] {getResources().getString(R.string.auto_language), getResources().getString(R.string.light_theme), getResources().getString(R.string.dark_theme)};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.drop_down_item, themeChoice);

        AutoCompleteTextView autoCompleteTextView2 = root.findViewById(R.id.filled_exposed_theme) ;
        autoCompleteTextView2.setAdapter(adapter2);

        // On met le spinner en fonction du thème actuel
        if (settings.getTheme().equals("auto")) {
            autoCompleteTextView2.setText(themeChoice[0], false);
        }
        else if (settings.getTheme().equals("light")) {
            autoCompleteTextView2.setText(themeChoice[1], false);
        }
        else if (settings.getTheme().equals("dark")) {
            autoCompleteTextView2.setText(themeChoice[2], false);
        }

        // Set the theme choice to the current theme
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(this, autoCompleteTextView2.getText().toString(), Toast.LENGTH_SHORT).show();
                Settings settings = MeccanocarManager.getInstance().getSettings();
                String theme = autoCompleteTextView2.getText().toString();
                if (theme.equals(themeChoice[0])) {
                    //MainActivity.setTheme(R.style.Theme_MeccanoCar); // Auto theme
                    settings.setTheme("auto");
                }
                else if (theme.equals(themeChoice[1])) {
                    //getActivity().setTheme(R.style.Theme_MeccanocarLight); // Light theme
                    settings.setTheme("light");
                }
                else if (theme.equals(themeChoice[2])) {
                    //getActivity().setTheme(R.style.Theme_MeccanocarDark); // Dark theme
                    settings.setTheme("dark");
                }

                // On sauvagarde les paramètres
                settings.saveSettings();

                // Recréez l'activité pour appliquer la nouvelle langue
                Intent intent = new Intent(getContext(), getActivity().getClass());
                startActivity(intent);
                getActivity().finish(); // Fermez l'activité actuelle
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
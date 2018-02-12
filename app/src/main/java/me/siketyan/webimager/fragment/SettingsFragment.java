package me.siketyan.webimager.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import me.siketyan.webimager.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setFilterEnabled(
            PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getBoolean("use_filter", false)
        );

        findPreference("use_filter")
            .setOnPreferenceChangeListener((pref, obj) -> {
                setFilterEnabled((boolean) obj);
                return true;
            });
    }

    private void setFilterEnabled(boolean isEnabled){
        findPreference("min_image_width").setEnabled(isEnabled);
        findPreference("min_image_height").setEnabled(isEnabled);
    }
}

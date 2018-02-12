package me.siketyan.webimager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import me.siketyan.webimager.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment fragment = new SettingsFragment();
        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, fragment)
            .commit();
    }
}

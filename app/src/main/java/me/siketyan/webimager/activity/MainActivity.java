package me.siketyan.webimager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import me.siketyan.webimager.R;
import me.siketyan.webimager.activity.ListActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);

        findViewById(R.id.go_button).setOnClickListener(view -> startActivity(
            new Intent(view.getContext(), ListActivity.class)
                .putExtra(
                    Intent.EXTRA_TEXT,
                    ((EditText) findViewById(R.id.uri_text)).getText()
                )
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(
                new Intent(this, SettingsActivity.class)
            );
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}

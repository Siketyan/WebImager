package me.siketyan.webimager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import me.siketyan.webimager.R;
import me.siketyan.webimager.handler.DownloadProgressHandler;
import me.siketyan.webimager.handler.ScannedImagesHandler;
import me.siketyan.webimager.object.Image;
import me.siketyan.webimager.runnable.DownloadImagesRunnable;
import me.siketyan.webimager.runnable.ScanImagesRunnable;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private String title;
    private List<Image> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_list);
        setSupportActionBar(toolbar);

        String uri = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        title = getIntent().getStringExtra(Intent.EXTRA_SUBJECT);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.scanning));
        dialog.show();

        new Thread(
            new ScanImagesRunnable(
                this, uri, new ScannedImagesHandler(this)
            )
        ).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_ok) {
            return super.onOptionsItemSelected(item);
        }

        startActivityForResult(
            new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                .putExtra("android.content.extra.SHOW_ADVANCED", true)
                .putExtra("android.content.extra.FANCY", true),
            334
        );

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) return;

        Uri treeUri = intent.getData();
        DocumentFile directory = DocumentFile.fromTreeUri(this, treeUri);

        grantUriPermission(
            getPackageName(),
            treeUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        );

        getContentResolver().takePersistableUriPermission(
            treeUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        );

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("create_sub_directory", true)) {
            directory = directory.createDirectory(title);
        }

        download(directory);
    }

    private void download(DocumentFile destination) {
        Image[] downloads = images.stream().filter(Image::isChecked).toArray(Image[]::new);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle(R.string.downloading);
        dialog.setMessage("");
        dialog.setMax(downloads.length);
        dialog.show();

        new Thread(
            new DownloadImagesRunnable(
                this,
                downloads,
                destination,
                new DownloadProgressHandler(this)
            )
        ).start();
    }

    public ProgressDialog getDialog() {
        return dialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}

package me.siketyan.webimager.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.ListView;
import me.siketyan.webimager.R;
import me.siketyan.webimager.activity.ListActivity;
import me.siketyan.webimager.adapter.ImageListAdapter;
import me.siketyan.webimager.object.Image;
import me.siketyan.webimager.object.ScanResult;

import java.util.List;

public class ScannedImagesHandler extends Handler {
    private ListActivity activity;

    public ScannedImagesHandler(ListActivity activity) {
        super(activity.getMainLooper());
        this.activity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
        if (msg.what == Activity.RESULT_OK) {
            ScanResult result = (ScanResult) msg.obj;
            List<Image> images = result.getImages();

            activity.setTitle(result.getTitle());
            activity.setImages(images);
            activity.getDialog().dismiss();

            ImageListAdapter adapter = new ImageListAdapter(activity, images);
            ListView listView = (ListView) activity.findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                CheckBox chk = (CheckBox) view.findViewById(R.id.check_box);
                chk.setChecked(!chk.isChecked());
            });
        } else if (msg.what == Activity.RESULT_CANCELED) {
            new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(R.string.error_scanning)
                .setPositiveButton(R.string.ok, (dialog, id) -> activity.finishAndRemoveTask())
                .show();
        }
    }
}

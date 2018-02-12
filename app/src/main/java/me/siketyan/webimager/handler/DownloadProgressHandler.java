package me.siketyan.webimager.handler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import me.siketyan.webimager.R;
import me.siketyan.webimager.activity.ListActivity;
import me.siketyan.webimager.object.DownloadProgress;
import org.apache.commons.io.FilenameUtils;

public class DownloadProgressHandler extends Handler {
    public static final int DOWNLOAD_PROGRESS = -2;

    private ListActivity activity;

    public DownloadProgressHandler(ListActivity activity) {
        super(activity.getMainLooper());
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        ProgressDialog dialog = activity.getDialog();

        if (msg.what == Activity.RESULT_OK) {
            dialog.dismiss();

            new AlertDialog.Builder(activity)
                .setMessage(R.string.success)
                .setPositiveButton(R.string.ok, (dialogInterface, id) -> activity.finishAndRemoveTask())
                .show();
        } else if (msg.what == Activity.RESULT_CANCELED) {
            new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(R.string.error_downloading)
                .setPositiveButton(R.string.ok, (dialogInterface, id) -> activity.finishAndRemoveTask())
                .show();
        } else if (msg.what == DOWNLOAD_PROGRESS) {
            DownloadProgress progress = (DownloadProgress) msg.obj;
            dialog.setMessage(FilenameUtils.getName(progress.getCurrent().getUri()));
            dialog.setProgress(progress.getIndex() + 1);
        }
    }
}

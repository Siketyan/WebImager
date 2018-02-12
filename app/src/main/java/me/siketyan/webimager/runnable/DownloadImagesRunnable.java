package me.siketyan.webimager.runnable;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import me.siketyan.webimager.handler.DownloadProgressHandler;
import me.siketyan.webimager.object.DownloadProgress;
import me.siketyan.webimager.object.Image;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImagesRunnable implements Runnable {
    private AppCompatActivity activity;
    private Image[] images;
    private DocumentFile destination;
    private Handler handler;

    public DownloadImagesRunnable(AppCompatActivity activity, Image[] images,
                                  DocumentFile destination, Handler handler) {
        this.activity = activity;
        this.images = images;
        this.destination = destination;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            int index = 0;
            for (Image image : images) {
                handler.sendMessage(
                    handler.obtainMessage(
                        DownloadProgressHandler.DOWNLOAD_PROGRESS,
                        new DownloadProgress(index++, image)
                    )
                );

                HttpURLConnection conn = (HttpURLConnection) new URL(image.getUri()).openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                DocumentFile file = destination.createFile(conn.getContentType(), FilenameUtils.getName(image.getUri()));
                try (InputStream in = conn.getInputStream();
                     OutputStream out = activity.getContentResolver().openOutputStream(file.getUri())
                ) {
                    IOUtils.copy(in, out);
                }
            }

            handler.sendMessage(
                handler.obtainMessage(Activity.RESULT_OK)
            );
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();

            handler.sendMessage(
                handler.obtainMessage(Activity.RESULT_CANCELED)
            );
        }
    }
}

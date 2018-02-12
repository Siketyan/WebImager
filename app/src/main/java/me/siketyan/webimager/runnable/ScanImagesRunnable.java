package me.siketyan.webimager.runnable;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import me.siketyan.webimager.object.Image;
import me.siketyan.webimager.object.ScanResult;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScanImagesRunnable implements Runnable {
    private AppCompatActivity activity;
    private String uri;
    private Handler handler;

    public ScanImagesRunnable(AppCompatActivity activity, String uri, Handler handler) {
        this.activity = activity;
        this.uri = uri;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(uri).get();
            String title = doc.title();
            List<Image> images = new ArrayList<>();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
            int minWidth = Integer.parseInt(pref.getString("min_image_width", "0"));
            int minHeight = Integer.parseInt(pref.getString("min_image_height", "0"));

            for (Element elem : doc.getElementsByTag("img")) {
                String uri = elem.attr("src");
                if (Uri.parse(uri).isRelative()) {
                    uri = new URL(
                        new URL(
                            this.uri.substring(
                                0,
                                FilenameUtils.indexOfLastSeparator(this.uri) + 1
                            )
                        ),
                        uri
                    ).toString();
                }

                boolean isChecked = true;
                if (pref.getBoolean("use_filter", false)) {
                    isChecked = !(
                        (elem.hasAttr("width") && Integer.parseInt(elem.attr("width")) < minWidth) ||
                        (elem.hasAttr("height") && Integer.parseInt(elem.attr("height")) < minHeight)
                    );
                }

                images.add(new Image(uri, isChecked));
            }

            handler.sendMessage(
                handler.obtainMessage(
                    Activity.RESULT_OK,
                    new ScanResult(title, images)
                )
            );
        } catch (Exception e) {
            e.printStackTrace();

            handler.sendMessage(
                handler.obtainMessage(Activity.RESULT_CANCELED)
            );
        }
    }
}

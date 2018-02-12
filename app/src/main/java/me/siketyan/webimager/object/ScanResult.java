package me.siketyan.webimager.object;

import me.siketyan.webimager.adapter.ImageListAdapter;

import java.util.List;

public class ScanResult {
    private String title;
    private List<Image> images;

    public ScanResult(String title, List<Image> images) {
        this.title = title;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public List<Image> getImages() {
        return images;
    }
}

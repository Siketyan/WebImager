package me.siketyan.webimager.object;

public class DownloadProgress {
    private int index;
    private Image current;

    public DownloadProgress(int index, Image current) {
        this.index = index;
        this.current = current;
    }

    public int getIndex() {
        return index;
    }

    public Image getCurrent() {
        return current;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCurrent(Image current) {
        this.current = current;
    }
}
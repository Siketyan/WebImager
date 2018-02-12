package me.siketyan.webimager.object;

public class Image {
    private String uri;
    private boolean isChecked;

    public Image(String uri, boolean isChecked) {
        this.uri = uri;
        this.isChecked = isChecked;
    }

    public String getUri() {
        return uri;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
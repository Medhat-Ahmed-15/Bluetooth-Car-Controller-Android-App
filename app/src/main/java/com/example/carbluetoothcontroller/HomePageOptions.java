package com.example.carbluetoothcontroller;

public class HomePageOptions {

    int image;
    String titletext;

    public HomePageOptions(int image, String titletext) {
        this.image = image;
        this.titletext = titletext;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitletext() {
        return titletext;
    }

    public void setTitletext(String titletext) {
        this.titletext = titletext;
    }
}

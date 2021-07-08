package com.mohamed_amgd.near_deal.Models;

public class Category {

    private String name;
    private int imageDrawable;

    public Category(String name, int imageDrawable) {
        this.name = name;
        this.imageDrawable = imageDrawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }
}

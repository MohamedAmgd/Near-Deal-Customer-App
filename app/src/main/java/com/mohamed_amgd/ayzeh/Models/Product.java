package com.mohamed_amgd.ayzeh.Models;

public class Product {
    private String name;
    private String category;
    private String brand;
    private String price;
    private String description;
    private String imageUrl;
    private String shopId;

    public Product(String name, String category, String brand, String price, String description, String imageUrl, String shopId) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}

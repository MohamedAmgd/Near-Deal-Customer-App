package com.mohamed_amgd.ayzeh.Models;

public class Offer {
    private String id;
    private String productId;
    private String shopId;
    private String shopName;
    private String shopImageUrl;
    private float price;
    private int amount;

    public Offer(String id, String productId, String shopId, String shopName, String shopImageUrl, float price, int amount) {
        this.id = id;
        this.productId = productId;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopImageUrl = shopImageUrl;
        this.price = price;
        this.amount = amount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

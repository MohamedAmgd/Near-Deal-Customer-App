package com.mohamed_amgd.near_deal.Models;

public class User {
    private String uid;
    private String email;
    private String username;
    private String birthdate;
    private String imageUrl;

    public User(String uid, String email, String username, String birthdate) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.birthdate = birthdate;
        imageUrl = null;
    }

    public User(String uid, String email, String username, String birthdate, String imageUrl) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.birthdate = birthdate;
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

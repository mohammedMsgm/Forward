package com.vogella.myapplication.Pojo;

import java.util.ArrayList;

public class Trainer {
    String name;
    String type;
    String bio;
    String clientsNumber;
    String phoneNumber;
    String reviews;
    String imageUrl;

    public ArrayList getImageUrls() {
        return imageUrls;
    }

    ArrayList<String> imageUrls;
    String currentClients;
    String documentPath;

    public String getDocumentPath() {
        return documentPath;
    }

    public String getInstegramAccount() {
        return instegramAccount;
    }

    String instegramAccount;

    public String getCurrentClients() {
        return currentClients;
    }

    public Trainer() {
    }

    public Trainer(String name, String type, String bio, String clientsNumber, String phoneNumber, String reviews, String imageUrl) {
        this.name = name;
        this.type = type;
        this.bio = bio;
        this.clientsNumber = clientsNumber;
        this.phoneNumber = phoneNumber;
        this.reviews = reviews;
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBio() {
        return bio;
    }

    public String getClientsNumber() {
        return clientsNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReviews() {
        return reviews;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}

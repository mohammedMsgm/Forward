package com.vogella.myapplication.Pojo;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

public class Item {
    String itemName;
    String imageUrl;
    ArrayList<String> imageUrls;
    String itemTrainerType;
    int price;
    int amount = 1;
    List<String> sizes;
    String phoneNumber;
    String documentPath;
    String bio;
    List<Long> ratings;
    CollectionReference events;
    int totalPrice;
    String category;
    double rating;
    boolean isTrainer = false;
    int selectedSize = 3;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Item(String itemName, String imageUrl, ArrayList<String> imageUrls, String category, int price, int amount, List<String> sizes, String phoneNumber, String documentPath, String bio, List<Long> ratings, int totalPrice, double rating, boolean isTrainer, int selectedSize) {
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.imageUrls = imageUrls;
        this.category = category;
        this.price = price;
        this.amount = amount;
        this.sizes = sizes;
        this.phoneNumber = phoneNumber;
        this.documentPath = documentPath;
        this.bio = bio;
        this.ratings = ratings;
        this.totalPrice = totalPrice;
        this.rating = rating;
        this.isTrainer = isTrainer;
        this.selectedSize = selectedSize;
    }

    public Item(String itemName, String itemTrainerType, String imageUrl, double rating, String documentPath, boolean isTrainer) {
        this.itemName = itemName;
        this.itemTrainerType = itemTrainerType;
        this.rating = rating;
        this.isTrainer = isTrainer;
        this.documentPath = documentPath;
        this.imageUrl = imageUrl;
    }
    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public String getBio() {
        return bio;
    }
    public List<Long> getRatings() {
        return ratings;
    }

    public CollectionReference getEvents() {
        return events;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Item() { }

    public List<String> getSizes() {
        return sizes;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public boolean isTrainer() {
        return isTrainer;
    }
    public String getItemTrainerType() {
        return itemTrainerType;
    }
    public String getDocumentPath() {
        return documentPath;
    }
    public double getRating() {
        return rating;
    }
    public String getItemName() {
        return itemName;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public int getPrice() {
        return price;
    }

    public int getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(int selectedSize) {
        this.selectedSize = selectedSize;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setItemTrainerType(String itemTrainerType) {
        this.itemTrainerType = itemTrainerType;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setRatings(List<Long> ratings) {
        this.ratings = ratings;
    }

    public void setEvents(CollectionReference events) {
        this.events = events;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTrainer(boolean trainer) {
        isTrainer = trainer;
    }
}
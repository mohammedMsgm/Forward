package com.vogella.myapplication.Pojo;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    String email;
    String password;
    String phone;
    String name;
    String profileImageUrl;
    String profileCoverUrl;
    String address;
    Date birthDate;
    List myEvents = new ArrayList();
    int mHeight;
    int mWeight;
    String qrCodeUrl;
    int maxPullUps, maxPushUps, maxDips;
    public User() {
    }

    public User(String email, String password, String phone, String name, String profileImageUrl, String address, Date birthDate, String qrCodeUrl, int mHeight, int mWeight, int maxPullUps, int maxPushUps, int maxDips, List myEvents ) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.address = address;
        this.birthDate = birthDate;
        this.name = name;
        this.qrCodeUrl = qrCodeUrl;
        this.maxDips = maxDips;
        this.maxPullUps = maxPushUps;
        this.maxPullUps = maxPullUps;
        this.mHeight = mHeight;
        this.mWeight = mWeight;
        this.myEvents = myEvents;
    }

    public void setMaxPullUps(int maxPullUps) {
        this.maxPullUps = maxPullUps;
    }

    public void setMaxPushUps(int maxPushUps) {
        this.maxPushUps = maxPushUps;
    }

    public void setMaxDips(int maxDips) {
        this.maxDips = maxDips;
    }


    public int getMaxPullUps() {
        return maxPullUps;
    }

    public int getMaxPushUps() {
        return maxPushUps;
    }

    public int getMaxDips() {
        return maxDips;
    }

    public int getmHeight() {
        return mHeight;
    }



    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileCoverUrl() {
        return profileCoverUrl;
    }


    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setheight(int height) {
        this.mHeight = height;
    }

    public int getmWeight() {
        return mWeight;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

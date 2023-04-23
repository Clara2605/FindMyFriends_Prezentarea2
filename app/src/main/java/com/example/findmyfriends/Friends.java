package com.example.findmyfriends;

public class Friends {
    private String username, name, profileImageUrl, currentLocation;

    public Friends(String username, String name, String profileImageUrl, String currentLocation) {
        this.username = username;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.currentLocation = currentLocation;
    }

    public Friends(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}

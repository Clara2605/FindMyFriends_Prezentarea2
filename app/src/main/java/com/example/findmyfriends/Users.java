package com.example.findmyfriends;

public class Users {
    private String name, email, username, profileImage, currentLocation;
    private double Longitude;
    private double Latitude;

    public Users(){

    }

    public Users(String name, String email, String username, String profileImage, String currentLocation, double longitude, double latitude) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
        this.currentLocation = currentLocation;
        Longitude = longitude;
        Latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}

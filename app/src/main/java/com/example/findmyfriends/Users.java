package com.example.findmyfriends;

public class Users {
    private String name, email, username, profileImage;

    public Users(){

    }

    public Users(String name, String email, String username, String profileImage) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
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



}

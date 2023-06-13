package com.RapidComments.ProdSense.Entitys;



public class AuthUser {
    final String id;
    final String email;
    final String username;
    final boolean emailVerified;
    final String picture;

    public AuthUser(String id, String email, String username, boolean emailVerified, String picture) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.emailVerified = emailVerified;
        this.picture = picture;
    }

    public String getUser_id() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public boolean getEmailVerified() {
        return emailVerified;
    }

    public String getPicture() {
        return picture;
    }
}
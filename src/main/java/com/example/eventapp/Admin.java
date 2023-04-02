package com.example.eventapp;

public class Admin {
    private String adminEmail;
    private String adminPassword;

    //Constructor with no parameter to hardcode an admin user. Very basic and inefficient but usable for current program.
    public Admin(){
        String adminEmail = "admin@gmail.com"; //assigning admin's email
        String adminPassword = "adm1n?";// assigning admin's password
    }

    //Method to get admin's email.
    public String getAdminEmail() {
        return adminEmail;
    }

    //Method to set admin's email.
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    //Method to get admin's password
    public String getAdminPassword() {
        return adminPassword;
    }

    //Method to get admin's password
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
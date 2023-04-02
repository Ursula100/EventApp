package com.example.eventapp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import static java.time.LocalDate.now;

public class Organiser implements Serializable {
    private static int next_Id = 21010003;
    private int oId;
    private String name;
    private String email;
    private String password;
    private String telephone;
    private String website;
    private String sMedia1;
    private String sMedia2;
    private boolean enabled;

    private static LocalDate dateRegistered;

    public Organiser(String name, String email, String password, String telephone, String website, String sMedia1, String sMedia2) {
        oId = next_Id++;
        this.email = email;
        this.name = name;
        this.telephone = telephone;
        this.website = website;
        this.sMedia1 = sMedia1;
        this.sMedia2 = sMedia2;
        dateRegistered = now();
        this.enabled = true;
        this.password = password;
    }

    //constructor for hard-coded organiser's
    public Organiser(int id, String name, String email, String password, String telephone){
        this.oId = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        website = " ";
        sMedia1 = " ";
        sMedia2 = " ";
        dateRegistered = now();
        //setDateRegistered(dateRegistered);
        this.enabled = true;
        this.password = password;
    }

    public static int getNext_Id() {
        return next_Id;
    }

    public static void setNext_Id(int next_Id) {
        Organiser.next_Id = next_Id;
    }

    public int getOId() {
        return oId;
    }

    public void setOId(int oId) {
        this.oId = oId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getsMedia1() {
        return sMedia1;
    }

    public void setsMedia1(String sMedia1) {
        this.sMedia1 = sMedia1;
    }

    public String getsMedia2() {
        return sMedia2;
    }

    public void setsMedia2(String sMedia2) {
        this.sMedia2 = sMedia2;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDate dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public String toString() {
        String isEnabled = (enabled) ? "Enabled": "Disabled" ;

        return "Organiser Id:" + oId +
                "Name:" + name + '\n' +
                "Email:" + email + '\n' +
                "Telephone:" + telephone + '\n' +
                "Website:" + website + '\n' +
                "Social Media Channel 1:" + sMedia1 + '\n' +
                "Social Media Channel 2:" + sMedia2 + '\n' +
                "Status" + enabled + '\n';
    }
}

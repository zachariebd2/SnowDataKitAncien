package com.example.neige.traitements;

import androidx.annotation.NonNull;

/**
 * @author Salah-Eddine ET-TALEBY
 * Classe pour créer/gérer les objets Formulaire
 */
public class Formulaire {
    private double latitude, longitude;
    private int accuracy, altitude, pourcentageNeige;
    private String date;
    private int id_user;
    private boolean selected;

    public Formulaire(String date, double latitude, double longitude, int accuracy, int altitude, int pourcentageNeige, int id_user) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.pourcentageNeige = pourcentageNeige;
        this.id_user = id_user;
        this.selected = false;
    }

    public String getDate() {
        return this.date;
    }

    public double getLatitude() {
        return (double) Math.round(this.latitude * 100) / 100;
    }

    public double getLongitude() {
        return (double) Math.round(this.longitude * 100) / 100;
    }

    public int getAccurracy() {
        return this.accuracy;
    }

    public int getAltitude() {
        return this.altitude;
    }

    public int getPourcentageNeige() {
        return this.pourcentageNeige;
    }

    public int getIdUser() {
        return this.id_user;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public void setPourcentageNeige(int pourcentageNeige) {
        this.pourcentageNeige = pourcentageNeige;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return getDate() + " " + getPourcentageNeige();
    }
}


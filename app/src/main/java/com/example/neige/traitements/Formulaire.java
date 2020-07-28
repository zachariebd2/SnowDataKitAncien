package com.example.neige.traitements;

public class Formulaire {
    private double latitude, longitude;
    private int accuracy, altitude, pourcentageNeige;
    private String date;
    private int id_user;

    public Formulaire(String date, double latitude, double longitude, int accuracy, int altitude, int pourcentageNeige, int id_user) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.pourcentageNeige = pourcentageNeige;
        this.id_user = id_user;
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

    @Override
    public String toString() {
        return "[" + this.getDate() + "] " + " Lat/Lng : (" + this.getLatitude() + ", " + this.getLongitude() + ")\n"
                + "Pourcentage de neige : " + this.getPourcentageNeige();
    }
}


package com.example.neige;

public class Formulaire {
    private double latitude, longitude;
    private int accuracy, altitude, pourcentageNeige;
    private String date;

    public Formulaire(String date, double latitude, double longitude, int accuracy, int altitude, int pourcentageNeige) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.pourcentageNeige = pourcentageNeige;
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

    @Override
    public String toString() {
        return "[" + this.getDate() + "] " + " Lat/Lng : (" + this.getLatitude() + ", " + this.getLongitude() + ")\n"
                + "Pourcentage de neige : " + this.getPourcentageNeige();
    }
}


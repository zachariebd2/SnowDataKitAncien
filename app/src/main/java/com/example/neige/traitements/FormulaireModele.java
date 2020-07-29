package com.example.neige.traitements;

public class FormulaireModele {

    public double latitude, longitude;
    public int altitude, pourcentageNeige;
    public String date;
    public boolean checked;

    public FormulaireModele(double latitude, double longitude, int altitude, int pourcentageNeige, String date, boolean checked) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.pourcentageNeige = pourcentageNeige;
        this.date = date;
        this.checked = checked;
    }
}

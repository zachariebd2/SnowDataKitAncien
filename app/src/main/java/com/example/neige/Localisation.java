package com.example.neige;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class Localisation extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView textLatLong, textAccAlt; // TextView de l'activité, altitude, longitude, latitude, précision
    private GoogleMap map; // Map Google
    private SupportMapFragment mapFragment; // Wrapper
    private float x1, x2, y1, y2; // Pour le swipe
    private double latitude, longitude; // Données affichées sur l'activité
    private int accuracy, altitude; // Données affichées sur l'activité
    private int saved_id_pourcentageNeige; // ID de l'input sauvegardé
    private String pseudo, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation);

        // Récupération des ID des TextView
        textLatLong = findViewById(R.id.textLatLong);
        textAccAlt = findViewById(R.id.textPrecision);

        // Instantation de la carte Google
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // Bundle pour stocker les "extras", c'est-à-dire les variables (int, float, String...)
        Bundle extras = getIntent().getExtras();
        // Si le bundle n'est pas null (= contient au moins une chaîne, ou un entier...)
        if (extras != null) {
            // On re-stocke tout dans de nouvelles variables
            int re_restoredAccuracy = extras.getInt("re_savedAccuracy");
            int re_restoredAltitude = extras.getInt("re_savedAltitude");
            double re_restoredLatitude = extras.getDouble("re_savedLatitude");
            double re_restoredLongitude = extras.getDouble("re_savedLongitude");
            saved_id_pourcentageNeige = extras.getInt("id_input_pourcentageNeige");
            pseudo = extras.getString("pseudo");
            id = extras.getString("id");

            // Et on affiche cela dans les TextView
            textAccAlt.setText("Précision : " + re_restoredAccuracy + "m");
            textLatLong.setText(getFormattedLocationInDegree(re_restoredLatitude, re_restoredLongitude) + " | Altitude : " + re_restoredAltitude + "m");
            getCurrentLocation();
        }

        // Fonction : Check des permissions au clic du bouton
        findViewById(R.id.buttonGetCurrentPosition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        // Récupération des permissions nécessaires (déclarées dans le fichier Manifest)
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            Localisation.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    getCurrentLocation();
                }
            }
        });
    }

    // Fonction : Éxécuter le code de la fonction getCurrentLocation si les permissions ont été check, afficher un message Toast le cas contraire
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) // Si le request code est le même que celui du check, et que le résultat du check est supérieur à 0
            getCurrentLocation();
        else
            Toast.makeText(this, "Permissions ron-autorisées !", Toast.LENGTH_SHORT).show();
    }

    private void getCurrentLocation() {
        // Déclaration de l'objet LocationRequest contenant les paramètres/services de FusedLocationProviderApi
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Check des permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        LocationServices.getFusedLocationProviderClient(Localisation.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Localisation.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            // Récupération de l'altitude, longitude...
                            latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            accuracy =
                                    (int) locationResult.getLocations().get(latestLocationIndex).getAccuracy();
                            altitude =
                                    (int) locationResult.getLocations().get(latestLocationIndex).getAltitude();

                            // Stockage des données dans les TextView
                            textLatLong.setText(getFormattedLocationInDegree(latitude, longitude) + " | Altitude : " + altitude + "m");
                            textAccAlt.setText("Précision : " + accuracy + "m");

                            // Affichage de la position sur la map
                            LatLng pos = new LatLng(latitude, longitude);
                            map.addMarker(new MarkerOptions().position(pos).title("Position actuelle"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(pos));
                            map.setMinZoomPreference(15);
                        }
                    }
                }, Looper.getMainLooper());
    }

    // Conversion de la latitude et de la longitude en format degrés
    public static String getFormattedLocationInDegree(double latitude, double longitude) {
        try {
            int latSeconds = (int) Math.round(latitude * 3600);
            int latDegrees = latSeconds / 3600;
            latSeconds = Math.abs(latSeconds % 3600);
            int latMinutes = latSeconds / 60;
            latSeconds %= 60;

            int longSeconds = (int) Math.round(longitude * 3600);
            int longDegrees = longSeconds / 3600;
            longSeconds = Math.abs(longSeconds % 3600);
            int longMinutes = longSeconds / 60;
            longSeconds %= 60;
            String latDegree = latDegrees >= 0 ? "N" : "S";
            String lonDegrees = longDegrees >= 0 ? "E" : "W";

            return "Lat : " + Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds
                    + "\"" + latDegree + " " + "| Long : " + Math.abs(longDegrees) + "°" + longMinutes
                    + "'" + longSeconds + "\"" + lonDegrees;
        } catch (Exception e) {
            return "" + String.format("%8.5f", latitude) + "  "
                    + String.format("%8.5f", longitude);
        }
    }

    // Chargement de la map, et style
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle)); // Style contenu dans le dossier raw > mapstyle en format JSON

            if (!success) Log.e("Localisation", "Parse du fichier JSON contenant le style échoué.");
        } catch (Resources.NotFoundException e) {
            Log.e("Localisation", "L'application ne trouve pas le fichier de style. Erreur : ", e);
        }
    }

    // Méthode pour Swipe vers la suite du formulaire
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                // Swipe vers la gauche
                if (x1 > x2) {
                    // Envoi des données vers l'activité NeigePourcentage
                    Intent i = new Intent(this, NeigePourcentage.class);
                    i.putExtra("savedLatitude", latitude);
                    i.putExtra("savedLongitude", longitude);
                    i.putExtra("savedAccuracy", accuracy);
                    i.putExtra("savedAltitude", altitude);
                    i.putExtra("saved_id_pourcentageNeige", saved_id_pourcentageNeige);
                    i.putExtra("pseudo", pseudo);
                    i.putExtra("id", id);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, Accueil.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
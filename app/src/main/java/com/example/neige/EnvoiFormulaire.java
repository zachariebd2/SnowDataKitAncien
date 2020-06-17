package com.example.neige;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EnvoiFormulaire extends AppCompatActivity {
    private static final String FILE_NAME = "formulaires.json";
    private int accuracy, altitude;
    private double latitude, longitude;
    private int pourcentageNeige;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envoi_formulaire);


        // Bundle pour stocker les extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Stockage des extras dans de nouvelles variables
            accuracy = extras.getInt("savedAccuracy");
            altitude = extras.getInt("savedAltitude");
            latitude = extras.getDouble("savedLatitude");
            longitude = extras.getDouble("savedLongitude");
            pourcentageNeige = Integer.parseInt(extras.getString("savedPourcentageNeige"));

        }

        findViewById(R.id.sauvegarderFormulaire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fos = null;
                File f = new File(getFilesDir(), FILE_NAME);
                if (!f.exists()) {
                    try {
                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(newFile().toString().getBytes());
                        Log.i("NEW_SAVED", "Nouveau fichier créée dans le rep. " + getFilesDir() + "/" + FILE_NAME);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                        fos.write(jsonData().toString().getBytes());
                        Log.i("SAVED", "Fichier sauvegardé dans le rep. " + getFilesDir() + "/" + FILE_NAME);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    private JSONObject jsonData() {
        JSONObject json = new JSONObject();
        try {
            JSONObject form = new JSONObject();
            form.put("id", 0);
            form.put("date", "");
            form.put("latitude", latitude);
            form.put("longitude", longitude);
            form.put("accuracy", accuracy);
            form.put("altitude", altitude);
            form.put("pourcentageNeige", pourcentageNeige);
            json.put("formulaires", new JSONArray()
                    .put(form));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private JSONObject newFile() {
        JSONObject json = new JSONObject();
        try {
            json.put("formulaires", new JSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Formulaires extends AppCompatActivity {
    private static final String TAG = "Formulaires";
    ArrayList<Formulaire> arrayList = new ArrayList<>();
    FormListAdapter arrayAdapter;
    String FILE_NAME = "formulaires.json";
    private float x1, x2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaires);
        Log.d(TAG, "onCreate: Started.");
        ListView liste_forms = findViewById(R.id.liste_formulaires);

        // Initialisation du JSON
        File f = new File(getFilesDir(), FILE_NAME);

        // Vérification
        if (!f.exists())
            Toast.makeText(this, "Il n'y a pour le moment, aucun formulaire à afficher.", Toast.LENGTH_SHORT).show();
        else {
            try {
                String formsStr = lireForm(f);
                // Fetch du JSON
                JSONObject obj = new JSONObject(formsStr);
                JSONArray formulaires = obj.getJSONArray("formulaires");
                for (int i = 0; i < formulaires.length(); i++) {
                    JSONObject form = formulaires.getJSONObject(i);
                    int pourcentageNeige = form.getInt("pourcentageNeige");
                    double latitude = form.getDouble("latitude");
                    double longitude = form.getDouble("longitude");
                    int accuracy = form.getInt("accuracy");
                    int altitude = form.getInt("altitude");
                    String date = form.getString("date");
                    Formulaire formulaire = new Formulaire(date, latitude, longitude, accuracy, altitude, pourcentageNeige);
                    arrayList.add(formulaire);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            // Initialisation de l'Array Adapter
            arrayAdapter = new FormListAdapter(this, R.layout.adapter_view_layout, arrayList);

            // Lier l'Array Adapter à la ListView
            liste_forms.setAdapter(arrayAdapter);

            // Afficher un message au clic d'un item
            liste_forms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Formulaires.this, arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Lire le contenu du fichier JSON et retourner le résultat dans une chaîne (String)
    private String lireForm(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    // Méthode pour Swipe vers la suite du formulaire
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                // Swipe vers la gauche
                if (x1 > x2) {
                    // Envoi des données vers l'activité ApresConnexion
                    Intent i = new Intent(this, ApresConnexion.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
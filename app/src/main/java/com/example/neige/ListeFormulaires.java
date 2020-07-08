package com.example.neige;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ListeFormulaires extends AppCompatActivity {

    // Initialisation des variables
    private ListView liste_forms;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String FILE_NAME = "formulaires.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_formulaires);

        // Initialisation des variables
        liste_forms = findViewById(R.id.liste_formulaires);

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
                    Formulaire formulaire = new Formulaire(date, latitude, longitude, accuracy, altitude, pourcentageNeige, 0);
                    arrayList.add(formulaire.toString());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            // Initialisation de l'Array Adapter
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

            // Lier l'Array Adapter à la ListView
            liste_forms.setAdapter(arrayAdapter);

            // Afficher un message au clic d'un item
            liste_forms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(ListeFormulaires.this, arrayList.get(position), Toast.LENGTH_SHORT).show();
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
}
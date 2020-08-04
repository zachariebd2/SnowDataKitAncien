package com.example.neige.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.neige.R;
import com.example.neige.myrequest.MyRequest;
import com.example.neige.traitements.FormListAdapter;
import com.example.neige.traitements.Formulaire;
import com.example.neige.traitements.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class FormulairesHorsLigne extends AppCompatActivity {
    private static final String TAG = "Formulaires";
    ArrayList<Formulaire> arrayList = new ArrayList<>();
    FormListAdapter arrayAdapter;
    private float x1, x2;
    private int id_user;
    private RequestQueue queue;
    private MyRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaires_hors_ligne);
        Log.d(TAG, "onCreate: Started.");
        ListView liste_forms = findViewById(R.id.liste_formulaires);

        // Bundle pour stocker les "extras", c'est-à-dire les variables (int, float, String...)
        Bundle extras = getIntent().getExtras();
        // Si le bundle n'est pas null (= contient au moins une chaîne, ou un entier...)
        if (extras != null) {
            id_user = extras.getInt("id_user");
        }

        // Initialisation du JSON
        File f = new File(getFilesDir(), "formulaires_" + id_user + ".json");


        // Instanciation de la requête Volley via la classe VolleySingleton (Google)
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

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
                    arrayList.add(formulaire);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            // Initialisation de l'Array Adapter
            arrayAdapter = new FormListAdapter(this, R.layout.adapter_view_layout, arrayList);

            // Lier l'Array Adapter à la ListView
            liste_forms.setAdapter(arrayAdapter);
        }

        // Afficher une boîte de dialogue
        liste_forms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // Toast.makeText(Formulaires.this, arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
                final AlertDialog dialog = new AlertDialog.Builder(FormulairesHorsLigne.this)
                        .setTitle("Envoi du formulaire")
                        .setMessage("Êtes-vous sûr(e) de vouloir envoyer ce formulaire ?")
                        .setPositiveButton("Oui", null)
                        .setNegativeButton("Non", null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        // Initialisation du JSON
                        File f = new File(getFilesDir(), "formulaires_" + id_user + ".json");
                        String formsStr = null;
                        try {
                            formsStr = lireForm(f);
                            // Fetch du JSON
                            JSONObject obj = new JSONObject(formsStr);
                            final JSONArray formulaires = obj.getJSONArray("formulaires");
                            double latitude = arrayList.get(position).getLatitude();
                            double longitude = arrayList.get(position).getLongitude();
                            int altitude = arrayList.get(position).getAltitude();
                            int pourcentageNeige = arrayList.get(position).getPourcentageNeige();
                            int accuracy = arrayList.get(position).getAccurracy();
                            String date = arrayList.get(position).getDate();
                            Formulaire formulaire = new Formulaire(date, latitude, longitude, accuracy, altitude, pourcentageNeige, id_user);

                            request.insertionFormulaire(formulaire, new MyRequest.InsertionFormCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    // Supression du formulaire envoyé de la liste
                                    arrayList.remove(position);
                                    arrayAdapter.notifyDataSetChanged();

                                    // Supression du formulaire envoyé du fichier JSON
                                    // TODO


                                    Toast.makeText(FormulairesHorsLigne.this, message, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void inputErrors(Map<String, String> errors) {
                                    if (errors.get("req") != null) {
                                        Toast.makeText(FormulairesHorsLigne.this, errors.get("req"), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialog.dismiss();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
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
                    Intent i = new Intent(this, Accueil.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
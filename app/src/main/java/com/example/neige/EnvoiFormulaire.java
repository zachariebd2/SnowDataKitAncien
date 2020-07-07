package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.example.neige.myrequest.MyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EnvoiFormulaire extends AppCompatActivity {
    private static final String FILE_NAME = "formulaires.json";
    private int accuracy, altitude;
    private double latitude, longitude;
    private int pourcentageNeige;
    private Button boutonSauvegarder;
    private float x1, x2;
    private int saved_id_pourcentageNeige;
    private RequestQueue queue;
    private MyRequest request;


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
            pourcentageNeige = extras.getInt("savedPourcentageNeige");
            saved_id_pourcentageNeige = extras.getInt("id_input_pourcentageNeige");
        }

        // Instanciation de la requête Volley via la classe VolleySingleton (Google)
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

        boutonSauvegarder = findViewById(R.id.sauvegarderFormulaire);

        // Clic sur le bouton "Sauvegarder hors-ligne"
        findViewById(R.id.sauvegarderFormulaire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject form;
                try {
                    File file = new File(getFilesDir(), FILE_NAME);
                    // Si le fichier n'existe pas, on en crée un, sinon on ajoute l'objet au fichier JSON existant
                    form = !file.exists() ? ajouterForm() : addFormToJson(lireForm(file));
                    stockerForm(form);
                    boutonSauvegarder.setEnabled(false); // On désactive le bouton
                    Toast.makeText(EnvoiFormulaire.this, "Le formulaire a été sauvegardé !", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Création du formulaire
        // On récupère la date du jour
        String date = getDateDuJour();
        final Formulaire formulaire = new Formulaire(date, latitude, longitude, accuracy, altitude, pourcentageNeige);

        // Bouton pour envoyer les données dans la BD
        Button boutonEnvoyer = findViewById(R.id.envoyerFormulaire);
        boutonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.insertionFormulaire(formulaire, new MyRequest.InsertionFormCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(EnvoiFormulaire.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inputErrors(Map<String, String> errors) {
                        if (errors.get("req") != null) {
                            Toast.makeText(EnvoiFormulaire.this, errors.get("req"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(EnvoiFormulaire.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Écrire le contenu dans le fichier JSON
    private void stockerForm(JSONObject jsonObject) throws IOException {
        String formStr = jsonObject.toString();
        File file = new File(getFilesDir(), FILE_NAME);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(formStr);
        bufferedWriter.close();
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

    // Ajouter un objet JSON à un fichier existant
    private JSONObject addFormToJson(String jsonStr) throws JSONException, IOException {
        JSONObject objetPrecedent = new JSONObject(jsonStr);
        JSONArray array = objetPrecedent.getJSONArray("formulaires");
        JSONObject form = dataJson();
        array.put(form);
        JSONObject objetJsonActuel = new JSONObject();
        objetJsonActuel.put("formulaires", array);
        return objetJsonActuel;
    }

    // Insertion des données dans un objet JSON qui sera retourné
    private JSONObject dataJson() throws JSONException, IOException {
        File file = new File(getFilesDir(), FILE_NAME);
        JSONObject form = new JSONObject();
        String date = getDateDuJour();

        form.put("id", !file.exists() ? 1 : recupererId(lireForm(file)) + 1);

        // On insère les données
        form.put("date", date);
        form.put("latitude", latitude);
        form.put("longitude", longitude);
        form.put("accuracy", accuracy);
        form.put("altitude", altitude);
        form.put("pourcentageNeige", pourcentageNeige);

        return form;
    }

    private String getDateDuJour() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        return dateFormat.format(d);
    }

    // Ajouter un nouveau objet JSON
    private JSONObject ajouterForm() throws JSONException, IOException {
        JSONObject formWrapper = new JSONObject();
        JSONObject form = dataJson();
        formWrapper.put("formulaires", new JSONArray()
                .put(form));
        return formWrapper;
    }

    // Récupérer l'ID du dernier objet dans le fichier "formulaires.json"
    private int recupererId(String jsonStr) throws JSONException {
        JSONObject obj = new JSONObject(jsonStr);

        // Tableau formulaires
        JSONArray forms = obj.getJSONArray("formulaires");

        // Récupérer le dernier objet
        JSONObject last_form = forms.getJSONObject(forms.length() - 1);

        return last_form.optInt("id");
    }

    // Swipe vers la gauche ou vers la droite
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: // Lorsque l'utilisateur clique sur l'écran sur l'écran
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP: // Lorsque l'utilisateur retire son doigt de l'écran
                x2 = touchEvent.getX();
                if (x1 < x2) {
                    Intent i = new Intent(this, NeigePourcentage.class);
                    // Données à renvoyer
                    i.putExtra("savedAccuracy", accuracy);
                    i.putExtra("savedAltitude", altitude);
                    i.putExtra("savedLongitude", longitude);
                    i.putExtra("savedLatitude", latitude);
                    i.putExtra("savedPourcentageNeige", pourcentageNeige);
                    i.putExtra("saved_id_pourcentageNeige", saved_id_pourcentageNeige);

                    startActivity(i); // On lance l'activité NeigePourcentage
                }
                break;
        }
        return false;
    }
}
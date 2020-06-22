package com.example.neige;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            pourcentageNeige = extras.getInt("savedPourcentageNeige");
        }

        findViewById(R.id.sauvegarderFormulaire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject form;
                try {
                    File file = new File(getFilesDir(), FILE_NAME);
                    if (!file.exists()) { // Si le fichier n'existe pas
                        form = ajouterForm();
                        Log.i("NEW_FORM", "Form sauvegardé.");
                    } else {
                        form = addFormToJson(lireForm(file)); // Dans le cas où le fichier existe déjà
                        Log.i("FORM_UPDATE", "Form sauvegardé.");
                    }
                    stockerForm(form);
                    Toast.makeText(EnvoiFormulaire.this, "Le formulaire a été sauvegardé !", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void stockerForm(JSONObject jsonObject) throws IOException {
        String formStr = jsonObject.toString();
        File file = new File(getFilesDir(), FILE_NAME);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(formStr);
        bufferedWriter.close();
    }

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

    private JSONObject addFormToJson(String jsonStr) throws JSONException, IOException {
        JSONObject objetPrecedent = new JSONObject(jsonStr);
        JSONArray array = objetPrecedent.getJSONArray("formulaires");
        JSONObject form = dataJson();
        array.put(form);
        JSONObject objetJsonActuel = new JSONObject();
        objetJsonActuel.put("formulaires", array);
        return objetJsonActuel;
    }

    private JSONObject dataJson() throws JSONException, IOException {
        File file = new File(getFilesDir(), FILE_NAME);
        JSONObject form = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String date = dateFormat.format(d);
        if (!file.exists()) {
            form.put("id", 1);
        } else {
            form.put("id", recupererId(lireForm(file)) + 1);
        }
        form.put("date", date);
        form.put("latitude", latitude);
        form.put("longitude", longitude);
        form.put("accuracy", accuracy);
        form.put("altitude", altitude);
        form.put("pourcentageNeige", pourcentageNeige);
        return form;
    }

    private JSONObject ajouterForm() throws JSONException, IOException {
        JSONObject formWrapper = new JSONObject();
        JSONObject form = dataJson();
        formWrapper.put("formulaires", new JSONArray()
                .put(form));
        return formWrapper;
    }

    private int recupererId(String jsonStr) throws JSONException {
        JSONObject obj = new JSONObject(jsonStr);

        // Tableau formulaires
        JSONArray forms = obj.getJSONArray("formulaires");

        // Récupérer le dernier objet
        JSONObject last_form = forms.getJSONObject(forms.length() - 1);

        return last_form.optInt("id");
    }
}
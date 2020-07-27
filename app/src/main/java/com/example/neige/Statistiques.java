package com.example.neige;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Salah-Eddine ET-TALEBY
 * Classe liée à l'activité Statistiques
 * L'utilisateur peut simplement consulter des données le concernant (nombre de formulaires envoyés, rang)
 */
public class Statistiques extends AppCompatActivity {
    String url = "https://neige.000webhostapp.com/statistiques.php";
    private int id_user;
    int nbFormsEnvoyes;
    TextView tv_nbFormsEnvoyes, tv_pseudo;
    String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        // Bundle pour stocker les "extras", c'est-à-dire les variables (int, float, String...)
        Bundle extras = getIntent().getExtras();
        // Si le bundle n'est pas null (= contient au moins une chaîne, ou un entier...)
        if (extras != null) {
            id_user = extras.getInt("id_user");
            pseudo = extras.getString("pseudo");
        }

        tv_nbFormsEnvoyes = findViewById(R.id.tv_nbFormsEnvoyes);
        tv_pseudo = findViewById(R.id.tv_pseudo);

        tv_pseudo.setText(pseudo);

        stats();
    }

    // Méthode qui crée une requête Volley pour dialoguer avec le serveur et la BD via le fichier statistiques.php
    public void stats() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("RESPONSE_LIST", response); // La réponse reçue par le serveur
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    nbFormsEnvoyes = object.getInt("nbFormsEnvoyes");
                                }
                                tv_nbFormsEnvoyes.setText("Nombre de formulaires envoyés : " + nbFormsEnvoyes);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Statistiques.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_user", id_user + ""); // $_POST["id_user"]
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
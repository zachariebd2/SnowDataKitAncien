/*
 * Copyright (c) Salah-Eddine ET-TALEBY, CESBIO 2020
 */

package com.example.neige.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
import com.example.neige.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Statistiques extends AppCompatActivity {
    String url = "http://osr-cesbio.ups-tlse.fr/sdk/statistiques.php";
    private int id_user;
    int nbFormsEnvoyes;
    double contribution;
    TextView tv_nbFormsEnvoyes, tv_pseudo;
    String pseudo;
    private float x1, x2;
    private TextView tv_loggeEnTantQue;
    TextView tv_contribution;

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
        tv_contribution = findViewById(R.id.tv_contribution);

        tv_loggeEnTantQue = findViewById(R.id.tv_loggeEnTantQue);
        tv_loggeEnTantQue.setText("Vous êtes loggé avec le compte " + pseudo);

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
                                    contribution = object.getDouble("contribution");
                                }
                                tv_nbFormsEnvoyes.setText("Nombre de formulaires envoyés : " + nbFormsEnvoyes);
                                tv_contribution.setText("Contribution : " + (double) Math.round(contribution * 100) / 100 + "%");
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

    // Méthode pour Swipe
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                // Swipe vers la gauche
                if (x1 < x2) {
                    Intent i = new Intent(this, Accueil.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
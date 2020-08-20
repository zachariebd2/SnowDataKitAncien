/*
 * Copyright (c) Salah-Eddine ET-TALEBY, CESBIO 2020
 */

package com.example.neige.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;
import com.example.neige.traitements.SessionManager;

public class GestionCompte extends AppCompatActivity {

    private String pseudo;
    private int id_user;
    private SessionManager sessionManager;
    private float x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_compte);

        // Initialisation de SessionManger
        sessionManager = new SessionManager(this);

        // Si l'utilisateur est loggé, on récupère les informations
        if (sessionManager.isLogged()) {
            pseudo = sessionManager.getPseudo();
            id_user = Integer.parseInt(sessionManager.getId());
        }

        // Ouvrir la fenêtre de changement de password
        Button btn_update_password = findViewById(R.id.btn_update_password);
        btn_update_password.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), UpdatePassword.class);
            i.putExtra("pseudo", pseudo);
            i.putExtra("id_user", id_user);
            startActivity(i);
            finish();
        });

        // Déconnecter l'utilisateur lorsqu'il clique sur le bouton "Déconnexion"
        Button btn_deconnexion = findViewById(R.id.btn_deconnexion);
        btn_deconnexion.setOnClickListener(v -> {
            sessionManager.logout();
            Intent i = new Intent(getApplicationContext(), Bienvenue.class);
            startActivity(i);
            finish();
        });
    }

    // Méthode pour Swipe
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = touchEvent.getX();

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
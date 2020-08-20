
/*
 * Copyright (c) Salah-Eddine ET-TALEBY, CESBIO 2020
 */

package com.example.neige.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;
import com.example.neige.traitements.SessionManager;

public class Bienvenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenue);

        // Récupération des boutons et de la session utilisateur
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_register);
        SessionManager sessionManager = new SessionManager(this);

        // Si l'utilisateur est déjà loggé, alors on le redirige vers l'application Accueil (activité principale)
        if (sessionManager.isLogged()) {
            Intent i = new Intent(getApplicationContext(), Accueil.class);
            startActivity(i);
            finish();
        }

        // Bouton "Se connecter"
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Connexion.class);
                startActivity(i);
            }
        });

        // Bouton "S'inscrire"
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Inscription.class);
                startActivity(i);
            }
        });
    }
}
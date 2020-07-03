package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ApresConnexion extends AppCompatActivity {

    private SessionManager sessionManager;
    private Button btn_listeformulaires, btn_nouveauformulaire, btn_deconnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apres_connexion);

        sessionManager = new SessionManager(this);
        btn_listeformulaires = (Button) findViewById(R.id.btn_listeformulaires);
        btn_nouveauformulaire = (Button) findViewById(R.id.btn_nouveauformulaire);
        btn_deconnexion = (Button) findViewById(R.id.btn_deconnexion);

        // Si l'utilisateur est loggé, on récupère les informations
        if (sessionManager.isLogged()) {
            String pseudo = sessionManager.getPseudo();
            String id = sessionManager.getId();
        }

        // Ouvrir la liste de formulaires
        btn_listeformulaires.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Formulaires.class);
                startActivity(i);
            }
        });

        // Ouvrir la fenêtre de localisation afin de saisir un nouveau formulaire
        btn_nouveauformulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Localisation.class);
                startActivity(i);
            }
        });

        // Déconnecter l'utilisateur lorsqu'il clique sur le bouton "Déconnexion"
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent i = new Intent(getApplicationContext(), Accueil.class);
                startActivity(i);
                finish();
            }
        });
    }
}
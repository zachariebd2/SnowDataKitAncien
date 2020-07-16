package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Accueil extends AppCompatActivity {

    private SessionManager sessionManager;
    private Button btn_listeformulaires_horsligne, btn_listeformulaires_bd, btn_nouveauformulaire, btn_deconnexion;
    private String pseudo, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apres_connexion);

        sessionManager = new SessionManager(this);
        btn_listeformulaires_horsligne = findViewById(R.id.btn_listeformulaires_horsligne);
        btn_listeformulaires_bd = findViewById(R.id.btn_listeformulaires_bd);
        btn_nouveauformulaire = findViewById(R.id.btn_nouveauformulaire);
        btn_deconnexion = findViewById(R.id.btn_deconnexion);

        // Si l'utilisateur est loggé, on récupère les informations
        if (sessionManager.isLogged()) {
            pseudo = sessionManager.getPseudo();
            id = sessionManager.getId();
        }

        // Ouvrir la liste de formulaires sauvegardés hors-ligne
        btn_listeformulaires_horsligne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Formulaires.class);
                startActivity(i);
            }
        });

        // Ouvrir la liste des formulaires sauvegardés dans la base de données
        btn_listeformulaires_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FormulairesBD.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id);
                startActivity(i);
            }
        });


        // Ouvrir la fenêtre de localisation afin de saisir un nouveau formulaire
        btn_nouveauformulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Localisation.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        // Déconnecter l'utilisateur lorsqu'il clique sur le bouton "Déconnexion"
        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent i = new Intent(getApplicationContext(), Bienvenue.class);
                startActivity(i);
                finish();
            }
        });
    }
}
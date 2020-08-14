/**
 * @author Salah-Eddine ET-TALEBY
 * Classe liée à l'activité principale...
 * L'utilisateur peut saisir un nouveau formulaire, consulter ses formulaires envoyés/sauvegardés, et voir ses statistiques
 */

package com.example.neige.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;
import com.example.neige.traitements.SessionManager;

public class Accueil extends AppCompatActivity {

    // Variables nécessaires
    private SessionManager sessionManager;
    private String pseudo;
    private int id_user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Instanciation des variables
        sessionManager = new SessionManager(this);
        Button btn_listeformulaires_horsligne = findViewById(R.id.btn_listeformulaires_horsligne);
        Button btn_listeformulaires_bd = findViewById(R.id.btn_listeformulaires_bd);
        Button btn_nouveauformulaire = findViewById(R.id.btn_nouveauformulaire);
        Button btn_deconnexion = findViewById(R.id.btn_deconnexion);
        Button btn_aide = findViewById(R.id.btn_aide);
        Button btn_statistiques = findViewById(R.id.btn_statistiques);
        Button btn_update_password = findViewById(R.id.btn_update_password);


        // Si l'utilisateur est loggé, on récupère les informations
        if (sessionManager.isLogged()) {
            pseudo = sessionManager.getPseudo();
            id_user = Integer.parseInt(sessionManager.getId());
        }

        TextView tv_loggeEnTantQue = findViewById(R.id.tv_loggeEnTantQue);
        tv_loggeEnTantQue.setText("Vous êtes loggé avec le compte " + pseudo);

        // Ouvrir la liste de formulaires sauvegardés hors-ligne
        btn_listeformulaires_horsligne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListeFormulairesHorsLigne.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id_user);
                startActivity(i);
                finish();
            }
        });

        // Ouvrir la liste des formulaires sauvegardés dans la base de données
        btn_listeformulaires_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FormulairesBD.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id_user);
                startActivity(i);
            }
        });


        // Ouvrir la fenêtre de localisation afin de saisir un nouveau formulaire
        btn_nouveauformulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Localisation.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id_user);
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

        // Ouvrir la fenêtre d'aide
        btn_aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(getApplicationContext(), ListeFormulairesHorsLigne.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id_user);
                startActivity(i);
                finish();*/
                Toast.makeText(Accueil.this, "Clic !", Toast.LENGTH_SHORT).show();
            }
        });

        // Ouvrir la fenêtre de statistiques
        btn_statistiques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Statistiques.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id_user);
                startActivity(i);
                finish();
            }
        });

        // Ouvrir la fenêtre de changement de password
        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdatePassword.class);
                i.putExtra("pseudo", pseudo);
                i.putExtra("id_user", id_user);
                startActivity(i);
                finish();
            }
        });
    }
}
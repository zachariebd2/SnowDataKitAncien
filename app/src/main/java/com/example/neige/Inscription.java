package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Inscription extends AppCompatActivity {

    private Button boutonInscription, boutonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Lors du clic sur le bouton Inscription
        boutonInscription = findViewById(R.id.boutonInscription);
        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Inscription.this, "Bouton inscription cliqué !", Toast.LENGTH_SHORT).show();
            }
        });

        // Lors du clic sur le bouton RetourVersConnexion
        boutonRetour = findViewById(R.id.boutonInscription);
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), Connexion.class);
                startActivity(i);
            }
        });
    }
}
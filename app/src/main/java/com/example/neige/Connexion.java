package com.example.neige;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.example.neige.myRequest.MyRequest;

public class Connexion extends AppCompatActivity {

    private EditText inputNom_Utilisateur, inputMDP;
    private Button boutonConnexion, boutonInscription;
    private RequestQueue queue;
    private MyRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        inputNom_Utilisateur = findViewById(R.id.nom_utilisateur);
        inputMDP = findViewById(R.id.mdp);
        boutonConnexion = (Button) findViewById(R.id.boutonConnexion);
        boutonInscription = (Button) findViewById(R.id.boutonInscription);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new MyRequest(this, queue);

        // Check des permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            return;

        // Bouton "Connexion"
        boutonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom_utilisateur = inputNom_Utilisateur.getText().toString().trim();
                String mdp = inputMDP.getText().toString().trim();

                if (nom_utilisateur.length() > 0 && mdp.length() > 0) {
                    request.connexion(nom_utilisateur, mdp, new MyRequest.LoginCallback() {
                        @Override
                        public void onSuccess(String id, String nom_utilisateur) {
                            Intent i = new Intent(getApplicationContext(), Localisation.class);
                            startActivity(i);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(Connexion.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Connexion.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Bouton "Inscription"
        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Inscription.class);
                startActivity(i);
            }
        });
    }
}
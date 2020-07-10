package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Bienvenue extends AppCompatActivity {

    private Button btn_login, btn_register;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        sessionManager = new SessionManager(this);

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
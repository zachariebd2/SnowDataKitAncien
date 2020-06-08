package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Connexion extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextView textViewInscription;
    private TextView textViewResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textInputUsername = findViewById(R.id.text_input_username);
        this.textInputPassword = findViewById(R.id.text_input_password);
        this.textViewInscription = findViewById(R.id.textView_Inscription);
        this.textViewResetPassword = findViewById(R.id.textView_ResetPassword);

    }

    private boolean validate(TextInputLayout textInputUsername) {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Ce champ ne peut pas être vide.");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }


    public void boutonConnexion(View v) {
        if (!validate(textInputPassword) | !validate(textInputUsername)) {
            return;
        }

        // C'est ici que l'on redigera l'utilisateur
        // Connexion >>> Page formulaire
        String input = "Username : " + textInputUsername.getEditText().getText().toString();
        input += "\n";
        input += "Password : " + textInputPassword.getEditText().getText().toString();

        // Affichage du message
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

    public void renvoiVersResetPassword(View view) {
        // Renvoie vers la page de réinitialisation du mot de passe
        Toast.makeText(this, "Bouton RESET cliqué !", Toast.LENGTH_SHORT).show();
    }

    public void renvoiVersInscription(View view) {
        // Renvoie vers la page d'inscription
        Toast.makeText(this, "Bouton Inscription cliqué !", Toast.LENGTH_SHORT).show();
    }

    // Méthode de test pour ouvrir l'activité Localisation
    public void ouvrirLocalisation(View v) {
        Button btn = (Button) findViewById(R.id.ouvrir_localisation);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, Localisation.class));
            }
        });
    }
}
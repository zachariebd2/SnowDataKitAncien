package com.example.neige.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;

public class NeigePourcentage extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private float x1, x2;
    private int restoredAccuracy, restoredAltitude, pourcentageNeige;
    private double restoredLatitude, restoredLongitude;
    private String pseudo;
    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neige_pourcentage);

        radioGroup = findViewById(R.id.radioForm);

        // Bundle pour stocker les extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Stockage des extras dans de nouvelles variables
            restoredAccuracy = extras.getInt("savedAccuracy");
            restoredAltitude = extras.getInt("savedAltitude");
            restoredLongitude = extras.getDouble("savedLongitude");
            restoredLatitude = extras.getDouble("savedLatitude");
            int restoredIdPourcentageNeige = extras.getInt("saved_id_pourcentageNeige");
            pseudo = extras.getString("pseudo");
            id_user = extras.getInt("id_user");

            // Restauration de l'input radio choisi
            radioGroup.check(restoredIdPourcentageNeige);
        }
    }

    public void check(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String pourcentageNeigeSplit = radioButton.getText().toString().substring(0, radioButton.getText().toString().length() - 1);
        pourcentageNeige = Integer.parseInt(pourcentageNeigeSplit);
    }

    // Swipe vers l'activité Localisation
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: // Lorsque l'utilisateur clique sur l'écran sur l'écran
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP: // Lorsque l'utilisateur retire son doigt de l'écran
                x2 = touchEvent.getX();
                if (x1 < x2) {
                    Intent i = new Intent(this, Localisation.class);
                    // Données de la localisation
                    i.putExtra("re_savedAccuracy", restoredAccuracy);
                    i.putExtra("re_savedAltitude", restoredAltitude);
                    i.putExtra("re_savedLongitude", restoredLongitude);
                    i.putExtra("re_savedLatitude", restoredLatitude);
                    i.putExtra("id_input_pourcentageNeige", radioGroup.getCheckedRadioButtonId());
                    i.putExtra("id_user", id_user);
                    i.putExtra("pseudo", pseudo);
                    startActivity(i); // On lance l'activité Localisation
                } else if (x1 > x2) {
                    Intent i = new Intent(this, EnvoiFormulaire.class);
                    // Données de la localisation
                    i.putExtra("savedAccuracy", restoredAccuracy);
                    i.putExtra("savedAltitude", restoredAltitude);
                    i.putExtra("savedLongitude", restoredLongitude);
                    i.putExtra("savedLatitude", restoredLatitude);
                    i.putExtra("savedPourcentageNeige", pourcentageNeige);
                    i.putExtra("id_input_pourcentageNeige", radioGroup.getCheckedRadioButtonId());
                    i.putExtra("id_user", id_user);
                    i.putExtra("pseudo", pseudo);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
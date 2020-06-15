package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NeigePourcentage extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView textView;
    private float x1, x2, y1, y2;
    private int restoredAccuracy, restoredAltitude;
    private double restoredLatitude, restoredLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neige_pourcentage);

        radioGroup = findViewById(R.id.radioForm);
        textView = findViewById(R.id.textView_selected);

        // Bundle pour stocker les extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Stockage des extras dans de nouvelles variables
            restoredAccuracy = extras.getInt("savedAccuracy");
            restoredAltitude = extras.getInt("savedAltitude");
            restoredLatitude = extras.getDouble("savedLatitude");
            restoredLongitude = extras.getDouble("savedLongitude");

        }

        // Bouton confirmer choix : On se contente de modifier le textView pour l'instant
        Button boutonConfirmer = findViewById(R.id.confirmer);
        boutonConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                textView.setText("Sélection : " + radioButton.getText());
                Log.i("ID RADIO", "ID du bouton radio choisi : " + radioButton.getId());
            }
        });
    }

    // Swipe vers l'activité Localisation
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: // Lorsque l'utilisateur clique sur l'écran sur l'écran
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP: // Lorsque l'utilisateur retire son doigt de l'écran
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2) {
                    Intent i = new Intent(this, Localisation.class);
                    // Données de la localisation
                    i.putExtra("re_savedAccuracy", restoredAccuracy);
                    i.putExtra("re_savedAltitude", restoredAltitude);
                    i.putExtra("re_savedLongitude", restoredLongitude);
                    i.putExtra("re_savedLatitude", restoredLatitude);
                    startActivity(i); // On lance l'activité Localisation
                }
                break;
        }
        return false;
    }
}
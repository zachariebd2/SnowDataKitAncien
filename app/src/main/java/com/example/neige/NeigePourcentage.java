package com.example.neige;

import android.content.Intent;
import android.os.Bundle;
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restoredAccuracy = extras.getInt("savedAccuracy");
            restoredAltitude = extras.getInt("savedAltitude");
            restoredLatitude = extras.getDouble("savedLatitude");
            restoredLongitude = extras.getDouble("savedLongitude");

        }

        Button boutonConfirmer = findViewById(R.id.confirmer);
        boutonConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                textView.setText("Sélection : " + radioButton.getText());
            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2) {
                    Intent i = new Intent(this, Localisation.class);
                    // Données de la localisation
                    i.putExtra("re_savedAccuracy", restoredAccuracy);
                    i.putExtra("re_savedAltitude", restoredAltitude);
                    i.putExtra("re_savedLongitude", restoredLongitude);
                    i.putExtra("re_savedLatitude", restoredLatitude);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
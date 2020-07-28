package com.example.neige.traitements;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;

public class PostgreSSH extends AppCompatActivity {
    private Button boutonReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postgre_s_s_h);

        boutonReq = findViewById(R.id.bReq);

        boutonReq.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
            }
        });


    }
}

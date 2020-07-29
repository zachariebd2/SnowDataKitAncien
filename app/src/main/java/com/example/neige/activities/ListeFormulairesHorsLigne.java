package com.example.neige.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;
import com.example.neige.traitements.CustomAdapter;
import com.example.neige.traitements.FormulaireModele;

import java.util.ArrayList;

public class ListeFormulairesHorsLigne extends AppCompatActivity {
    private ArrayList dataForms;
    ListView listView;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_formulaires_hors_ligne);

        listView = (ListView) findViewById(R.id.liste_forms);

        dataForms = new ArrayList();

        dataForms.add(new FormulaireModele(42, 59.3, 150, 50, "12/01/2020", true));
        dataForms.add(new FormulaireModele(75.55, 128, 154, 30, "13/01/2020", false));
        dataForms.add(new FormulaireModele(12, 44, 21, 20, "12/05/2020", true));
        dataForms.add(new FormulaireModele(-57, 65.5, 98, 0, "17/09/2020", false));

        adapter = new CustomAdapter(dataForms, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FormulaireModele formulaire = (FormulaireModele) dataForms.get(position);
                formulaire.checked = !formulaire.checked;
                adapter.notifyDataSetChanged();
            }
        });

    }
}
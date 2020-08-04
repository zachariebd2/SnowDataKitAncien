package com.example.neige.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neige.R;

import java.util.ArrayList;

public class ListeFormulairesHorsLigne extends AppCompatActivity {
    private ArrayList<String> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_formulaires_hors_ligne);

        ListView ch1 = findViewById(R.id.lv_formulairesHL);
        ch1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] items = {"Date : 21/06/2020\nLatitude - Longitude : 42.452, 78.456", "Date : 24/06/2020\nLatitude - Longitude : 42.452, 78.456", "Date : 26/06/2020\nLatitude - Longitude : 42.452, 78.456"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout, R.id.txt_form, items);
        ch1.setAdapter(adapter);
        ch1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem);
                } else {
                    selectedItems.add(selectedItem);
                }
            }
        });
    }

    public void envoyerFormulairesHL(View view) {
        String items = "";
        for (String item : selectedItems) {
            items += "- " + item + "\n";
        }
        Toast.makeText(this, "Items choisis : \n" + items, Toast.LENGTH_SHORT).show();
    }
}
package com.example.neige.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.neige.traitements.FormListAdapter;
import com.example.neige.traitements.Formulaire;
import com.example.neige.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormulairesBD extends AppCompatActivity {
    private ListView listView;
    private FormListAdapter adapter;
    public static ArrayList<Formulaire> formulaireArrayList = new ArrayList();

    String url = "http://osr-cesbio.ups-tlse.fr/sdk/retrieve.php";
    private Formulaire formulaire;
    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaires_b_d);

        listView = findViewById(R.id.liste_formulaires_bd);
        adapter = new FormListAdapter(this, R.layout.adapter_view_layout, formulaireArrayList);
        listView.setAdapter(adapter);

        // Bundle pour stocker les "extras", c'est-à-dire les variables (int, float, String...)
        Bundle extras = getIntent().getExtras();
        // Si le bundle n'est pas null (= contient au moins une chaîne, ou un entier...)
        if (extras != null) {
            id_user = extras.getInt("id_user");
        }

        retrieveData();
    }

    public void retrieveData() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        formulaireArrayList.clear();
                        try {
                            Log.d("RESPONSE_LIST", response); // La réponse reçue par le serveur
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String f_id = object.getString("f_id");
                                    int pourcentageNeige = object.getInt("f_pourcentageneige");
                                    Double latitude = object.getDouble("f_latitude");
                                    Double longitude = object.getDouble("f_longitude");
                                    int accuracy = object.getInt("f_accuracy");
                                    int altitude = object.getInt("f_altitude");
                                    String date = object.getString("f_date");

                                    formulaire = new Formulaire(date, latitude, longitude, accuracy, altitude, pourcentageNeige, id_user);
                                    formulaireArrayList.add(formulaire);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FormulairesBD.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_user", id_user + ""); // $_POST["id_user"]
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
package com.example.neige;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormulairesBD extends AppCompatActivity {
    private ListView listView;
    private FormListAdapter adapter;
    public static ArrayList<Formulaire> formulaireArrayList = new ArrayList();

    String url = "https://neige.000webhostapp.com/retrieve.php";
    private Formulaire formulaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaires_b_d);

        listView = findViewById(R.id.liste_formulaires_bd);
        adapter = new FormListAdapter(this, R.layout.adapter_view_layout, formulaireArrayList);
        listView.setAdapter(adapter);

        retrieveData();
    }

    public void retrieveData() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        formulaireArrayList.clear();
                        try {
                            Log.d("RESPONSE_LIST", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String f_id = object.getString("f_id");
                                    int pourcentageNeige = object.getInt("f_pourcentageNeige");
                                    Double latitude = object.getDouble("f_latitude");
                                    Double longitude = object.getDouble("f_longitude");
                                    int accuracy = object.getInt("f_accuracy");
                                    int altitude = object.getInt("f_altitude");
                                    String date = object.getString("f_date");
                                    int id_user = object.getInt("f_id_user");

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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
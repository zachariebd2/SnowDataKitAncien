package com.example.neige.myRequest;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest {

    private Context context;
    private RequestQueue queue;

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    // Fonction pour l'inscription
    public void inscription(String nom_utilisateur, String mdp, String mdp2) {
        String url = "http://192.168.0.20/neige_app/inscription.php";
        // ...
    }

    public void connexion(final String nom_utilisateur, final String mdp, final LoginCallback callback) {
        String url = "http://192.168.0.20/neige_app/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error) {
                        String id = json.getString("id");
                        String nom_utilisateur = json.getString("nom_utilisateur");
                        callback.onSuccess(id, nom_utilisateur);
                    } else {
                        callback.onError(json.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    callback.onError("Impossible de se connecter (NetworkError).");
                } else if (error instanceof VolleyError) {
                    callback.onError("Une erreur s'est produite (Volley).");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("nom_utilisateur", nom_utilisateur);
                map.put("mdp", mdp);
                return map;
            }
        };

        queue.add(request);
    }

    // Interface Inscription
    public interface RegisterCallback {
        void onSuccess(String message);

        void inputErrors(Map<String, String> errors);

        void onError(String message);
    }

    // Interface Login
    public interface LoginCallback {
        void onSuccess(String id, String nom_utilisateur);

        void onError(String message);
    }


}

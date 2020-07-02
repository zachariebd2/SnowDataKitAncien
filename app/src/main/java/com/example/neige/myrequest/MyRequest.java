package com.example.neige.myrequest;

import android.content.Context;
import android.util.Log;

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

    public void register(final String pseudo, final String email, final String password, final String password2, final RegisterCallback callback) {
        String url = "https://neige.000webhostapp.com/register.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();
                try {
                    Log.d("response_convert_err", "[" + response + "]");
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error) {
                        // Inscription OK !
                        callback.onSuccess("Vous êtes bien inscrit !");

                    } else {
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("pseudo")) {
                            errors.put("pseudo", messages.getString("pseudo"));
                        }
                        if (messages.has("email")) {
                            errors.put("email", messages.getString("email"));
                        }
                        if (messages.has("password")) {
                            errors.put("password", messages.getString("password"));
                        }
                        callback.inputErrors(errors);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    callback.onError("NetworkError : Impossible de se connecter !");
                } else if (error instanceof VolleyError) {
                    callback.onError("VolleyError : Une erreur s'est produite...");
                }
            }
        }) {
            // Envoi des paramètres que l'on veut tester dans le fichier register.php
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("pseudo", pseudo); // $_POST["pseudo"]
                map.put("email", email);
                map.put("password", password);
                map.put("password2", password2);
                return map;
            }
        };
        queue.add(request);
    }

    public void login(final String pseudo, final String password, final LoginCallback callback) {
        String url = "https://neige.000webhostapp.com/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if (!error) {
                        String id = json.getString("id");
                        String pseudo = json.getString("pseudo");
                        callback.onSuccess(id, pseudo);
                    } else {
                        callback.onError(json.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onError("Une erreur s'est produite...");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    callback.onError("NetworkError : Impossible de se connecter !");
                } else if (error instanceof VolleyError) {
                    callback.onError("VolleyError : Une erreur s'est produite...");
                }
            }
        }) {
            // Envoi des paramètres que l'on veut tester dans le fichier register.php
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("pseudo", pseudo); // $_POST["pseudo"]
                map.put("password", password);
                return map;
            }
        };
        queue.add(request);
    }

    public interface RegisterCallback {
        void onSuccess(String message);

        void inputErrors(Map<String, String> errors);

        void onError(String message);
    }

    public interface LoginCallback {
        void onSuccess(String id, String pseudo);

        void onError(String message);
    }
}

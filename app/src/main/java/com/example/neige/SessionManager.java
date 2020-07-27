package com.example.neige;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Salah-Eddine ET-TALEBY
 * Classe pour gérer la session de l'utilisateur (savoir si celui-ci est connecté, et si tel est le cas => transmission des infos (pseudo, id) aux activités qui le demandent)
 */
public class SessionManager {
    // Variables nécessaires
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // Constantes
    private final static String PREFS_NAME = "app_prefs";
    private final static int PRIVATE_MODE = 0;
    private final static String IS_LOGGED = "isLogged";
    private final static String PSEUDO = "pseudo";
    private final static String ID = "id";

    private Context context;

    // Constructeur de la classe
    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    // Retourne la valeur de IS_LOGGED
    public boolean isLogged() {
        return prefs.getBoolean(IS_LOGGED, false);
    }

    // Retourne le pseudo de l'utilisateur
    public String getPseudo() {
        return prefs.getString(PSEUDO, null);
    }

    // Retourne l'ID de l'utilisateur
    public String getId() {
        return prefs.getString(ID, null);
    }

    // Insère le pseudo/ID de l'utilisateur dans les SharedPreferences
    public void insertUser(String id, String pseudo) {
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(ID, id);
        editor.putString(PSEUDO, pseudo);
        editor.commit();
    }

    // Déconnecte l'utilisateur (nettoyage des SharedPreferences)
    public void logout() {
        editor.clear().commit();
    }
}

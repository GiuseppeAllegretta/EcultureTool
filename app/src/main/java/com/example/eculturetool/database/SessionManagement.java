package com.example.eculturetool.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe che si occupa di gestire la sessione dell'utente
 */
public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Metodo che si occupa di salvare la sessione
     * @param emailCuratore da salvare in modo tale da recuperare la sessione
     */
    public void saveSession(String emailCuratore) {
        editor.putString(SESSION_KEY, emailCuratore).commit();
    }

    /**
     * Metoto che prende i riferimento della sessione e li restituisce
     * @return riferimenti della sessione
     */
    public String getSession() {
        return sharedPreferences.getString(SESSION_KEY, "-1");
    }

    /**
     * Metodo che rimuove la sessione
     */
    public void removeSession() {
        editor.putString(SESSION_KEY, "-1").commit();
    }
}

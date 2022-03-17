package com.example.eculturetool.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.eculturetool.entities.Curatore;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String idCuratore) {
       editor.putString(SESSION_KEY, idCuratore).commit();
    }

    public String getSession() {
        return sharedPreferences.getString(SESSION_KEY,"-1");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"-1").commit();
    }
}

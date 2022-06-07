package com.example.eculturetool.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

/**
 * Classe che si occupa della gestione dei permessi richiesti dall'app per l'esecuzione di determinate funzioni.
 * In particolare, vengono richiesti i permessi per l'utilizzo della fotocamera, per la scrittura su storage esterno
 * e, infine, si assicura della presenza o meno di una connessione ad internet.
 */
public class Permissions extends AppCompatActivity {

    /**
     * Codice della richiesta per il permesso di accesso allo storage esterno
     */
    public static final int STORAGE_REQUEST_CODE = 100;

    /**
     * Codice della richiesta per il permesso di accesso alla fotocamera
     */
    public static final int CAMERA_REQUEST_CODE = 110;

    /**
     * Costruttore aparametrico
     */
    public Permissions() {}

    /**
     * Metodo che controlla se è stato concesso o no il permesso di accesso allo storage esterno.
     * @param activity l'activity chiamante
     * @return si il permesso è stato concesso, no altrimenti
     */
    public boolean checkStoragePermission(Activity activity) {
        return (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Metodo che controlla se è stato concesso o no il permesso di accesso alla fotocamera.
     * @param activity l'activity chiamante
     * @return si il permesso è stato concesso, no altrimenti
     */
    public boolean checkCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Metodo che consente la visualizzazione di una Snackbar personalizzata permanente, possiede
     * il tasto OK per essere dismessa.
     * @param parentLayout parent layout in cui far comparire la Snackbar
     * @param text messaggio da visualizzare
     * @return visualizzazione della Snackbar personalizzata permanente
     */
    public Snackbar getPermanentSnackBarWithOkAction(View parentLayout, String text) {
        Snackbar snackBar = Snackbar.make(parentLayout, text, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        return snackBar;
    }

    /**
     * Metodo che richiede il permesso di accesso allo storage esterno.
     * @param activity l'activity chiamante
     */
    public void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    /**
     * Metodo che richiede il permesso di accesso alla fotocamera.
     * @param activity l'activity chiamante
     */
    public void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
    }

    /**
     * Metodo che controlla la disponibilità o meno di una connessione ad internet
     * @param context Informazioni globali dell'applicativo
     * @return si se la connessione ad internet è disponibile, no altrimenti
     */
    public boolean checkConnection(Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                result = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                result = true;
            }
        }
        return result;
    }

    /**
     *
     * @param message messaggio da visualizzare
     * @param okListener listner scelta affermativa
     * @param activity l'activity chiamante
     */
    public void showMessageOkCancel(String message, DialogInterface.OnClickListener okListener, Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}

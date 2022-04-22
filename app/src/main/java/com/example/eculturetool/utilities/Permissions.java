package com.example.eculturetool.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.eculturetool.activities.UploadImageActivity;
import com.google.android.material.snackbar.Snackbar;

public class Permissions extends AppCompatActivity {

    public static final String STORAGE_PERMISSION_MSG = "Per usare questa funzionalità è necessario consentire l'accesso a risorse esterne.";
    public static final String CAMERA_PERMISSION_MSG = "Per usare questa funzionalità è necessario consentire l'accesso alla fotocamera.";
    public static final int STORAGE_REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 110;

    public Permissions() {
    }

    public boolean checkStoragePermission(Activity activity, View parentLayout) {
        return (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkCameraPermission(Activity activity, View parentLayout) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

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

    public void requestStoragePermission(Activity activity, View parentLayout) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    public void requestCameraPermission(Activity activity, View parentLayout) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
    }

    public void showMessageOkCancel(String message, DialogInterface.OnClickListener okListener, Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}

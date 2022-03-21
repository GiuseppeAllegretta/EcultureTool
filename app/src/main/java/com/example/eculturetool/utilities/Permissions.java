package com.example.eculturetool.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class Permissions extends AppCompatActivity {

    public static final String STORAGE_PERMISSION_MSG = "Per usare questa funzionalità è necessario consentire l'accesso a risorse esterne.";
    public static final String CAMERA_PERMISSION_MSG = "Per usare questa funzionalità è necessario consentire l'accesso alla fotocamera.";
    static Permissions permissions;

    public static Permissions getInstance() {
        if (permissions == null)
            permissions = new Permissions();
        return permissions;
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

    public void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    public void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    public boolean checkStoragePermission(Activity activity, View parentLayout) {
        boolean res2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!res2) {
            Snackbar snackBar = getPermanentSnackBarWithOkAction(parentLayout, STORAGE_PERMISSION_MSG);
            snackBar.show();
        }
        return res2;
    }

    public boolean checkCameraPermission(Activity activity, View parentLayout) {
        boolean res1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!(res1 && res2)) {
            Snackbar snackBar = getPermanentSnackBarWithOkAction(parentLayout, CAMERA_PERMISSION_MSG);
            snackBar.show();
        }
        return res1 && res2;
    }
}

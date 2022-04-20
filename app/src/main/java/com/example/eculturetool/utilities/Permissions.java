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

    public Permissions() {

    }

    public boolean checkStoragePermission(Activity activity, View parentLayout) {
        boolean res = false;
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            Snackbar snackBar = getPermanentSnackBarWithOkAction(parentLayout, STORAGE_PERMISSION_MSG);
            snackBar.show();
        }
        else{
            res = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return res;
    }

    public boolean checkCameraPermission(Activity activity, View parentLayout) {
        boolean res = false;
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            Snackbar snackBar = getPermanentSnackBarWithOkAction(parentLayout, STORAGE_PERMISSION_MSG);
            snackBar.show();
        }
        else{
            res = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;;
        }
        return res;
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
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    public void requestCameraPermission(Activity activity, View parentLayout) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
    }

}

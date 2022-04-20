package com.example.eculturetool.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.NonNull;
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
            Snackbar snackBar = getPermanentSnackBarWithOkAction(parentLayout, CAMERA_PERMISSION_MSG);
            snackBar.show();
        }
        else{
            res = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;;
        }
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 110) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (!(grantResult == PackageManager.PERMISSION_GRANTED)) {

                    }
                }
            }
        }
        if (requestCode == 110) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (!(grantResult == PackageManager.PERMISSION_GRANTED)) {

                    }
                }
            }
        }
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

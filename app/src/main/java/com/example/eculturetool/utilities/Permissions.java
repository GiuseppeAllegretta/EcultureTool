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
    public static final int STORAGE_REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 110;

    public Permissions() {
    }

    public boolean checkStoragePermission(Activity activity, View parentLayout) {
        return (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED);
    }

    public boolean checkCameraPermission(Activity activity, View parentLayout) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
                        Snackbar snackBar = getPermanentSnackBarWithOkAction(findViewById(android.R.id.content), STORAGE_PERMISSION_MSG);
                        snackBar.show();
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
                        Snackbar snackBar = getPermanentSnackBarWithOkAction(findViewById(android.R.id.content), CAMERA_PERMISSION_MSG);
                        snackBar.show();
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
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
    }

    public void requestCameraPermission(Activity activity, View parentLayout) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
    }

}

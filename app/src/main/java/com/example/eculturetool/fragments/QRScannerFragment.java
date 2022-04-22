package com.example.eculturetool.fragments;

import static com.example.eculturetool.activities.UploadImageActivity.CAMERA_PERMISSION_MSG;
import static com.example.eculturetool.utilities.Permissions.CAMERA_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.eculturetool.R;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.snackbar.Snackbar;

public class QRScannerFragment extends Fragment {

    Permissions permission = new Permissions();
    private CodeScanner mCodeScanner;
    private View parentLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final Activity activity = getActivity();
        parentLayout = getActivity().findViewById(android.R.id.content);
        View root = null;
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        if (activity != null) {
            if(permission.checkCameraPermission(activity, parentLayout)){
                root = inflater.inflate(R.layout.fragment_q_r_scanner, container, false);
                mCodeScanner.setDecodeCallback(decodeCallBack -> activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, decodeCallBack.getText(), Toast.LENGTH_SHORT).show();
                    }
                }));
                scannerView.setOnClickListener(onClickListener -> mCodeScanner.startPreview());
            }else{
                mCodeScanner.stopPreview();
                root = inflater.inflate(R.layout.fragment_q_r_scanner_2, container, false);
                permission.requestCameraPermission(activity, parentLayout);
            }
        }
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions perm = new Permissions();
        if (requestCode == CAMERA_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (!(grantResult == PackageManager.PERMISSION_GRANTED)) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

                            perm.showMessageOkCancel(CAMERA_PERMISSION_MSG,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    perm.requestCameraPermission(getActivity(), parentLayout);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, CAMERA_PERMISSION_MSG);
                                                    snackBar.show();
                                                    break;
                                            }
                                        }
                                    }, getActivity());
                        } else {
                            Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, "Consenti l'accesso alla fotocamera dalle impostazioni per usare questa funzionalità");
                            snackBar.show();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
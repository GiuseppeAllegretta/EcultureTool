package com.example.eculturetool.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.eculturetool.R;

public class QRScannerFragment extends Fragment {
    private CodeScanner mCodeScanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_q_r_scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        if (activity != null) {
            mCodeScanner = new CodeScanner(activity, scannerView);
            mCodeScanner.setDecodeCallback(decodeCallBack -> activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, decodeCallBack.getText(), Toast.LENGTH_SHORT).show();
                }
            }));
            scannerView.setOnClickListener(onClickListener -> mCodeScanner.startPreview());
        }
        return root;
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
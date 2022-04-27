package com.example.eculturetool.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.DettaglioOggettoActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.Capture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRcodeScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRcodeScannerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    Button scanBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public QRcodeScannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRcodeScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRcodeScannerFragment newInstance(String param1, String param2) {
        QRcodeScannerFragment fragment = new QRcodeScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bottone per la scansione del QR code
        scanBtn = view.findViewById(R.id.scanBtn);

        scanQRcode();
    }

    private void scanQRcode() {
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                //set prompt
                intentIntegrator.setPrompt("Per il flash usa il tasto volume su");
                intentIntegrator.setBeepEnabled(true);
                //looked orientation
                intentIntegrator.setOrientationLocked(true);
                //set campture activity
                intentIntegrator.setCaptureActivity(Capture.class);
                //initialize scan
                intentIntegrator.initiateScan();
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_rcode_scanner, container, false);
    }
}
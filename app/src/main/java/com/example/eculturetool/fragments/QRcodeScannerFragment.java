package com.example.eculturetool.fragments;


import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eculturetool.R;

import com.example.eculturetool.utilities.Capture;
import com.getkeepsafe.taptargetview.TapTarget;

import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.zxing.integration.android.IntentIntegrator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRcodeScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRcodeScannerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    Button scanBtn;
    ImageView showTutorial;

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
        showTutorial=view.findViewById(R.id.showTutorialQr);

        scanQRcode();

        showTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorial();
            }
        });
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



    private void showTutorial(){


        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(scanBtn, "Scansiona QR Code", "Inquadra un Qr code di un\n" +
                        "oggetto per visualizzarlo,\n modificarlo o eliminarlo")
                        // All options below are optional
                        .outerCircleColor(R.color.gialloSecondario)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.verdePrimario)
                        .titleTextSize(15)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(12)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(false)
                        .transparentTarget(false)
                        .targetRadius(100),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                    }
                });

    }
}
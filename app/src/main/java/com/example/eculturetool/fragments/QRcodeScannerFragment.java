package com.example.eculturetool.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.DettaglioOggettoActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.Capture;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;


public class QRcodeScannerFragment extends Fragment {

    ActivityResultLauncher<ScanOptions> activityResultLaucher;
    Button scanBtn;
    ImageView showTutorial;


    public QRcodeScannerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String regex_id = "[0-9]*"; //espressione regolare utile per verificare che quanto letto dal qr code contenga solo numeri

        DataBaseHelper dataBaseHelper = new DataBaseHelper(requireContext());
        List<Oggetto> oggettiList = dataBaseHelper.getAllOggetti();
        activityResultLaucher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() != null) {
                        if (Pattern.matches(regex_id, result.getContents())) {
                            int idOggetto = Integer.parseInt(result.getContents());

                            for (Oggetto oggetto : oggettiList) {
                                if (oggetto.getId() == idOggetto) {
                                    List<Zona> zoneList = dataBaseHelper.getZone();

                                    Intent intent = new Intent(requireActivity(), DettaglioOggettoActivity.class);
                                    intent.putExtra(Oggetto.Keys.ID, idOggetto);
                                    intent.putExtra("ZONELIST", (Serializable) zoneList);

                                    startActivity(intent);
                                }
                            }
                        } else {
                            dialogOggettoNonTrovato();
                        }
                    } else {
                        dialogOggettoNonTrovato();
                    }
        });
    }

    private void dialogOggettoNonTrovato() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getResources().getString(R.string.avviso));
        builder.setMessage(getString(R.string.nessun_oggetto_trovato));
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            //dismiss dialog
            dialogInterface.dismiss();
        });
        //show alert dialog
        builder.create().show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Bottone per la scansione del QR code
        scanBtn = view.findViewById(R.id.scanBtn);
        showTutorial=view.findViewById(R.id.showTutorialQr);

        scanQRcode();

        showTutorial.setOnClickListener(view1 -> showTutorial());
    }


    private void scanQRcode() {
        scanBtn.setOnClickListener(view -> {
            ScanOptions options = new ScanOptions();
            //set prompt
            options.setPrompt(getString(R.string.flash));
            options.setBeepEnabled(true);
            //looked orientation
            options.setOrientationLocked(true);
            //set campture activity
            options.setCaptureActivity(Capture.class);
            //initialize scan
            activityResultLaucher.launch(options);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_rcode_scanner, container, false);
    }

    private void showTutorial(){
        TapTargetView.showFor(requireActivity(),                 // `this` is an Activity
                TapTarget.forView(scanBtn, getString(R.string.scansiona_qr_code), getString(R.string.inquadra))
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
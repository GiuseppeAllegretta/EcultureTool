package com.example.eculturetool.activities;

import static com.example.eculturetool.utilities.Permissions.CAMERA_PERMISSION_MSG;
import static com.example.eculturetool.utilities.Permissions.CAMERA_REQUEST_CODE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.databinding.HomeBinding;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.fragments.HomeFragment;
import com.example.eculturetool.fragments.ProfileFragment;
import com.example.eculturetool.fragments.QRScannerFragment;
import com.example.eculturetool.fragments.QRcodeScannerFragment;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity {

    HomeBinding binding;
    final String REF = "https://auth-96a19-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(REF);
    private DatabaseReference myRef;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private View parentLayout;
    protected Curatore curatore;
    private Permissions permission = new Permissions();


    private Integer perc = 0;
    private Integer ogg = 0;
    //private EditText numeroPercorso, anno, tipologia;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        parentLayout = findViewById(android.R.id.content);
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.qr_scanner:
                    //replaceFragment(new QRScannerFragment());
                    if(permission.checkCameraPermission(this, parentLayout)){
                        replaceFragment(new QRcodeScannerFragment());
                        //replaceFragment(new QRScannerFragment());
                    }else{
                        permission.requestCameraPermission(this, parentLayout);
                    }
                    break;
            }
            return true;
        });

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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                            perm.showMessageOkCancel(CAMERA_PERMISSION_MSG,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    perm.requestCameraPermission(HomeActivity.this, parentLayout);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, CAMERA_PERMISSION_MSG);
                                                    snackBar.show();
                                                    break;
                                            }
                                        }
                                    }, this);
                        } else {
                            Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, "Consenti l'accesso alla fotocamera dalle impostazioni per usare questa funzionalità");
                            snackBar.show();
                        }
                    }
                }
            }
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addPerc(View view) {
        //  myRef.child("percorso "+perc.toString()).setValue("vuoto");
        perc++;
    }

    public void addOgg(View view) {

        //  Oggetto oggetto= new Oggetto(nome.getText().toString(),anno.getText().toString(),tipologia.getText().toString());
        //  myRef.child("percorso "+numeroPercorso.getText().toString())
        //          .child("oggetto "+ogg.toString())
        //         .setValue(oggetto);
        ogg++;
    }


    /**
     * Metodo che riceve in input i dati che si raccolgono dal QR code
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //DA QUI
        String regex_id = "[0-9]*"; //espressione regolare utile per verificare che quanto letto dal qr code contenga solo numeri

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        List<Oggetto> oggettiList = dataBaseHelper.getAllOggetti();

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult.getContents() != null) {
            if (Pattern.matches(regex_id, intentResult.getContents())) {
                int idOggetto = Integer.parseInt(intentResult.getContents());
                System.out.println("Valore risultante: " + idOggetto);

                for (Oggetto oggetto : oggettiList) {
                    if (oggetto.getId() == idOggetto) {
                        List<Zona> zoneList = dataBaseHelper.getZone();

                        Intent intent = new Intent(this, DettaglioOggettoActivity.class);
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
    }

    private void dialogOggettoNonTrovato() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.avviso));
        builder.setMessage("Nessun oggetto trovato");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss dialog
                dialogInterface.dismiss();
            }
        });
        //show alert dialog
        builder.create().show();
    }


//funzione per uscire dall'app al doppio click
    @Override
    public void onBackPressed() {
        if (isInFragment() && !doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "press double tap to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (isInFragment()) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isInFragment() {
        for (Fragment item : getSupportFragmentManager().getFragments()) {
            if (item.isVisible() && (
                    "ProfileFragment".equals(item.getClass().getSimpleName()) ||
                            "HomeFragment".equals(item.getClass().getSimpleName()) ||
                            "QRcodeScannerFragment".equals(item.getClass().getSimpleName()))) {
                return true;
            }
        }
        return false;
    }


}

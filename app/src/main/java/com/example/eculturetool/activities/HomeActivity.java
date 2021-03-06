package com.example.eculturetool.activities;

import static com.example.eculturetool.utilities.Permissions.CAMERA_REQUEST_CODE;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eculturetool.R;
import com.example.eculturetool.databinding.HomeBinding;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.fragments.HomeFragment;
import com.example.eculturetool.fragments.ProfileFragment;
import com.example.eculturetool.fragments.QRcodeScannerFragment;
import com.example.eculturetool.fragments.SearchFragment;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.snackbar.Snackbar;

/**
 * Classe principale rappresentante la schermata che l'utente si ritrova all'avvio dell'app.
 * Permette di visualizzare i menu e di navigare nelle varie sezioni di cui l'app è costituita.
 */
public class HomeActivity extends AppCompatActivity {

    /**
     * binding: necessario per le interazioni con la view
     * parentLayout: layout grafico a monte dell'interfaccia
     * curatore: contiene i dati relativi all'utente loggato
     * permission: oggetto di tipo Permission, utilizzato per la gestione dei permessi
     * doubleBackToExitPressedOnce: flag per consentire di uscire dall'app con la doppia pressione del tasto indietro
     */
    HomeBinding binding;
    private View parentLayout;
    protected Curatore curatore;
    private Permissions permission = new Permissions();
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        parentLayout = findViewById(android.R.id.content);
        Fragment homeFragment = new HomeFragment();
        replaceFragment(homeFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:

                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.qr_scanner:
                    if(permission.checkCameraPermission(this)){
                        replaceFragment(new QRcodeScannerFragment());
                    }else{
                        permission.requestCameraPermission(this);
                    }
                    break;

                case R.id.cerca:
                    replaceFragment(new SearchFragment());
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

                            perm.showMessageOkCancel(getString(R.string.msg_permesso_fotocamera),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    perm.requestCameraPermission(HomeActivity.this);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, getString(R.string.msg_permesso_fotocamera));
                                                    snackBar.show();
                                                    break;
                                            }
                                        }
                                    }, this);
                        } else {
                            Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, getString(R.string.accesso_fotocamera));
                            snackBar.show();
                        }
                    }
                }
            }
        }
    }

    /**
     * Permette di intercambiare i fragment del menù in basso, spostandosi tra le varie sezioni dell'app
     * @param fragment, il fragment da caricare
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }


    //funzione per uscire dall'app al doppio click
    @Override
    public void onBackPressed() {
        if (isInFragment() && !doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.doppio_tap), Toast.LENGTH_SHORT).show();
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

    /**
     * Permette di sapere se un fragment è stato selezionato ed è attivo
     * @return boolean, true se ci si trova in un fragment, false altrimenti
     */
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

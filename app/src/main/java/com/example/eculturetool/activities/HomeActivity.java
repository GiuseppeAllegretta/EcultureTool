package com.example.eculturetool.activities;

import static com.example.eculturetool.utilities.Permissions.CAMERA_PERMISSION_MSG;
import static com.example.eculturetool.utilities.Permissions.CAMERA_REQUEST_CODE;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

//Only if you want to do something here. If you dont want then leave it blankToast.makeText(this, "Landscape Mode", Toast.LENGTH_SHORT).show();

        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

//Only if you want to do something here too.Toast.makeText(this, "Portrait Mode", Toast.LENGTH_SHORT).show();
        }
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

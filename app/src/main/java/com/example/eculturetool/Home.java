package com.example.eculturetool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eculturetool.databinding.HomeBinding;
import com.example.eculturetool.fragments.HomeFragment;
import com.example.eculturetool.fragments.PlacesFragment;
import com.example.eculturetool.fragments.ProfileFragment;
import com.example.eculturetool.fragments.QRScannerFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    HomeBinding binding;

    private Integer perc =0;
    private Integer ogg=0;
    private EditText numeroPercorso, nome, anno, tipologia;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://auth-96a19-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Intent intent = getIntent();
        //uid= intent.getStringExtra("uid");
        //myRef = database.getReference("general").child(uid);
        //myRef.setValue("empty");
        replaceFragment(new HomeFragment());







        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.places:
                    replaceFragment(new PlacesFragment());
                    break;
                case R.id.qr_scanner:
                    replaceFragment(new QRScannerFragment());
                    break;
            }

            return true;
        });

    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void addPerc(View view) {
        myRef.child("percorso "+perc.toString()).setValue("vuoto");
        perc++;
    }

    public void addOgg(View view) {

        Oggetto oggetto= new Oggetto(nome.getText().toString(),anno.getText().toString(),tipologia.getText().toString());
        myRef.child("percorso "+numeroPercorso.getText().toString())
                .child("oggetto "+ogg.toString())
                .setValue(oggetto);
        ogg++;
    }
}

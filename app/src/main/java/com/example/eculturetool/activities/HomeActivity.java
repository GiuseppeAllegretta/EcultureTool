package com.example.eculturetool.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eculturetool.R;
import com.example.eculturetool.databinding.HomeBinding;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.fragments.HomeFragment;
import com.example.eculturetool.fragments.ProfileFragment;
import com.example.eculturetool.fragments.QRScannerFragment;
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

    protected Curatore curatore;


    private Integer perc = 0;
    private Integer ogg = 0;
    //private EditText numeroPercorso, anno, tipologia;

    private String uid;
    private String nome;
    private TextView tv;
    float x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1888);

        }

        /**
         Intent intent = getIntent();
         uid = intent.getStringExtra("uid");
         myRef = database.getReference("curatori").child(uid);



         myRef.addValueEventListener(new ValueEventListener() {

        @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
        tv = findViewById(R.id.nomeUtente);
        curatore = snapshot.getValue(Curatore.class);
        }

        @Override public void onCancelled(@NonNull DatabaseError error) {

        }
        });
         **/

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.places:
                    startActivity(new Intent(getApplicationContext(), AggiungiOggettoActivity.class));
                    //replaceFragment(new PlacesFragment());
                    break;
                case R.id.qr_scanner:
                    replaceFragment(new QRScannerFragment());
                    break;
            }

            return true;
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if (x1 < x2) {
                    replaceFragment(new QRScannerFragment());
                }
                break;
        }
        return false;
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


}

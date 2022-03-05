package com.example.eculturetool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eculturetool.databinding.HomeBinding;
import com.example.eculturetool.fragments.HomeFragment;
import com.example.eculturetool.fragments.PlacesFragment;
import com.example.eculturetool.fragments.ProfileFragment;
import com.example.eculturetool.fragments.QRScannerFragment;

public class Home extends AppCompatActivity {

    HomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
}

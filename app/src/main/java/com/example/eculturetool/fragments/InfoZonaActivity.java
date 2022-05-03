package com.example.eculturetool.fragments;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;

public class InfoZonaActivity extends AppCompatActivity {

    ImageView close;

    public InfoZonaActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_zona);

        close = findViewById(R.id.ic_close);
        close.setOnClickListener(v -> finish());
    }
    
}

package com.example.eculturetool.fragments;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;

public class InfoZonaActivity extends AppCompatActivity {

    ImageView close;
    TextView titolo;


    public InfoZonaActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info_zona);
        titolo = findViewById(R.id.titolo);
        String title = getIntent().getStringExtra("TITLE");
        titolo.setText(title);

        close = findViewById(R.id.ic_close);
        close.setOnClickListener(v -> finish());
    }
    
}

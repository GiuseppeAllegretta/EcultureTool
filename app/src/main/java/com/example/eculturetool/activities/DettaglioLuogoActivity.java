package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Luogo;

public class DettaglioLuogoActivity extends AppCompatActivity {

    private TextView nomeLuogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_luogo);

        //Dichiarazione degli elementi della View
        nomeLuogo = findViewById(R.id.nomeLuogoDettaglio);

        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        Luogo luogo = (Luogo) intent.getSerializableExtra("LUOGO");

        nomeLuogo.setText(luogo.getNome());


    }
}
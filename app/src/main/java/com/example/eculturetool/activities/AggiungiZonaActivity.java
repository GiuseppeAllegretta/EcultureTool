package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;

public class AggiungiZonaActivity extends AppCompatActivity {
    private Connection connection = new Connection();

    private EditText nomeZona, descrizioneZona,numeroOggetti;
    private Button creaZona;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_zona);
        nomeZona=findViewById(R.id.nome_zona_add);
        descrizioneZona=findViewById(R.id.descrizione_zona_add);
        numeroOggetti=findViewById(R.id.numeroOggetti_zona_add);
        progressBar=findViewById(R.id.progressAddZona);
        creaZona=findViewById(R.id.creaZona);
    }

    @Override
    protected void onStart() {
        super.onStart();
        creaZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creazioneZona();
            }
        });
    }

    private void creazioneZona() {
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeZona.setError("Il nome della zona è richiesto");
            nomeZona.requestFocus();
            return;
        }
        if (descrizione.isEmpty()) {
            descrizioneZona.setError("La descrizione è richiesta");
            descrizioneZona.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        //Implementare scrittura della zona sul Realtime Database



    }
}
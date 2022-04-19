package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AggiungiZonaActivity extends AppCompatActivity {

    private EditText nomeZona, descrizioneZona,numeroOggetti;
    private Button creaZona;
    private ProgressBar progressBar;
    private static final int MAX_OGGETTI = 10;
    private List<Zona> zoneList = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    private int luogoCorrente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_zona);
        nomeZona=findViewById(R.id.nome_zona_add);
        descrizioneZona=findViewById(R.id.descrizione_zona_add);
        numeroOggetti=findViewById(R.id.numeroOggetti_zona_add);
        progressBar=findViewById(R.id.progressAddZona);
        creaZona=findViewById(R.id.creaZona);
        dataBaseHelper = new DataBaseHelper(this);

        luogoCorrente = dataBaseHelper.getIdLuogoCorrente();
        if (zoneList != null)
            zoneList = dataBaseHelper.zoneQuery();

    }

    @Override
    protected void onStart() {
        super.onStart();
        creaZona.setOnClickListener(view -> creazioneZona());

    }

    private void creazioneZona() {
        int numeroMax;
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();
        String numeroMaxString=numeroOggetti.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeZona.setError(getResources().getString(R.string.nome_zona_richiesto));
            nomeZona.requestFocus();
            return;
        }
        if (controlloEsistenzaNomeZona(nome)) {
            nomeZona.requestFocus();
            nomeZona.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneZona.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneZona.requestFocus();
            return;
        }

        if(numeroMaxString.isEmpty()){
            numeroMaxString=String.valueOf(MAX_OGGETTI);
            numeroMax = Integer.parseInt(numeroMaxString);
        }else
            numeroMax=Integer.parseInt(numeroMaxString);

        if(numeroMax>MAX_OGGETTI){
            numeroOggetti.setError(getResources().getString(R.string.numero_max));
            numeroOggetti.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        dataBaseHelper.aggiungiZona(new Zona(0, nome, descrizione, Integer.parseInt(numeroMaxString), luogoCorrente));
        zoneList = dataBaseHelper.zoneQuery();
        finish();

        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean controlloEsistenzaNomeZona(String nomeZona) {
        boolean isEsistente = false;
        nomeZona = this.nomeZona.getText().toString();

        for (int i = 0; i < zoneList.size(); i++) {
            if (nomeZona.compareToIgnoreCase(zoneList.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }

        return isEsistente;
    }
}
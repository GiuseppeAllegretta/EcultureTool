package com.example.eculturetool.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;
import java.util.List;


public class AggiungiZonaActivity extends AppCompatActivity {

    private EditText nomeZona, descrizioneZona;
    private Button creaZona;
    private ProgressBar progressBar;
    private List<Zona> zoneList = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    private int luogoCorrente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_zona);
        nomeZona=findViewById(R.id.nome_zona_add);
        descrizioneZona=findViewById(R.id.descrizione_zona_add);
        progressBar=findViewById(R.id.progressAddZona);
        creaZona=findViewById(R.id.creaZona);
        dataBaseHelper = new DataBaseHelper(this);

        luogoCorrente = dataBaseHelper.getIdLuogoCorrente();
        if (zoneList != null)
            zoneList = dataBaseHelper.getZone();

        getSupportActionBar().setTitle(getString(R.string.crea_zona));

    }

    @Override
    protected void onStart() {
        super.onStart();
        creaZona.setOnClickListener(view -> creazioneZona());

    }

    private void creazioneZona() {
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeZona.setError(getResources().getString(R.string.nome_zona_richiesto));
            nomeZona.requestFocus();
            return;
        }
        if (controlloEsistenzaNomeZona()) {
            nomeZona.requestFocus();
            nomeZona.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneZona.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneZona.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        dataBaseHelper.aggiungiZona(new Zona(0, nome, descrizione, luogoCorrente));
        zoneList = dataBaseHelper.getZone();
        finish();

        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean controlloEsistenzaNomeZona() {
        boolean isEsistente = false;
        String nomeZona = this.nomeZona.getText().toString();

        for (int i = 0; i < zoneList.size(); i++) {
            if (nomeZona.compareToIgnoreCase(zoneList.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }

        return isEsistente;
    }
}
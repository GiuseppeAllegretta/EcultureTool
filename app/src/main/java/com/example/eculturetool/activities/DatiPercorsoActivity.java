package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Percorso;

import java.util.List;

public class DatiPercorsoActivity extends AppCompatActivity {

    private Button confermaPercorsoBtn;
    private EditText nomePercorsoEdt;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dati_percorso);

        confermaPercorsoBtn = findViewById(R.id.confermaPercorso);
        nomePercorsoEdt = findViewById(R.id.nomePercorsoEdt);
        dataBaseHelper = new DataBaseHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        confermaPercorsoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatiPercorso();
            }
        });

    }

    private void getDatiPercorso() {

        if(nomePercorsoEdt.getText().toString().isEmpty()){
            nomePercorsoEdt.setError(getString(R.string.inserisci_nome_percorso));
            nomePercorsoEdt.requestFocus();
            return;
        }

        if(esistenzaNomePercorso()){
            nomePercorsoEdt.setError(getString(R.string.nome_esistente));
            nomePercorsoEdt.requestFocus();
            return;
        }

        Percorso percorso = new Percorso(nomePercorsoEdt.getText().toString(), dataBaseHelper.getIdLuogoCorrente());
        int idPercorso = dataBaseHelper.addPercorso(percorso);

        if(idPercorso != -1){
            percorso.setId(idPercorso);

            Intent intent = new Intent(this, CreazionePercorsoActivity.class);
            intent.putExtra("PERCORSO", percorso);

            startActivity(intent);
        }else {
            Toast.makeText(this, "Si è verificato un errore! \n Riprova", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean esistenzaNomePercorso() {
        boolean risultato = false;

        List<Percorso> percorsi = dataBaseHelper.getPercorsi();

        for(Percorso percorso: percorsi){
            if(percorso.getNome().compareToIgnoreCase(nomePercorsoEdt.getText().toString()) == 0){
                risultato = true;
            }
        }

        return risultato;
    }
}
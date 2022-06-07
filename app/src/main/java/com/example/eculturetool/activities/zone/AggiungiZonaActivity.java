package com.example.eculturetool.activities.zone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        //prendo i riferimenti delle view del layout
        nomeZona=findViewById(R.id.nome_zona_add);
        descrizioneZona=findViewById(R.id.descrizione_zona_add);
        progressBar=findViewById(R.id.progressAddZona);
        creaZona=findViewById(R.id.creaZona);
        dataBaseHelper = new DataBaseHelper(this);
        //prendo le zone relative ad un luogo
        luogoCorrente = dataBaseHelper.getIdLuogoCorrente();
        if (zoneList != null)
            zoneList = dataBaseHelper.getZone();

        getSupportActionBar().setTitle(getString(R.string.crea_zona));

    }

    @Override
    protected void onStart() {
        super.onStart();
        //crea una nuova zona
        creaZona.setOnClickListener(view -> {
            creazioneZona();
        });

    }

    private void creazioneZona() {
        Handler handler = new Handler(getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                //Messaggio che rende invisibile la progressBar
                if (message.what == 1) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                //La progressbar diventa visibile
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();

        if (nome.isEmpty()) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            nomeZona.setError(getResources().getString(R.string.nome_zona_richiesto));
            nomeZona.requestFocus();
            return;
        }
        if (controlloEsistenzaNomeZona()) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            nomeZona.requestFocus();
            nomeZona.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        if (descrizione.isEmpty()) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            descrizioneZona.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneZona.requestFocus();
            return;
        }
        if(dataBaseHelper.aggiungiZona(new Zona(0, nome, descrizione, luogoCorrente))){
            zoneList = dataBaseHelper.getZone();
            finish();
        }else{
            Toast.makeText(this, getResources().getString(R.string.db_errore_scrittura), Toast.LENGTH_LONG).show();
        }
    }
    //controllo unicità della zona
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
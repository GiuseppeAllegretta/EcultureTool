package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;

import java.util.List;

public class ModificaOggettoActivity extends AppCompatActivity {

    private Connection connection = new Connection();

    private EditText nomeOggetto, descrizioneOggetto, zonaAppartenenza;
    private Spinner tipologiaOggetto;
    private ImageView frecciaBack, conferma;

    //Si recupera questa lista per fare in modo che l'utente non crei/modifichi un luogo con lo stesso nome di uno già creato
    private List<Oggetto> oggettiList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_oggetto);


        nomeOggetto = findViewById(R.id.edit_name_oggetto);
        descrizioneOggetto = findViewById(R.id.editDescrizione);
        zonaAppartenenza = findViewById(R.id.editZona);
        tipologiaOggetto = findViewById(R.id.spinner_tipologia_oggetti_edit);
        frecciaBack = findViewById(R.id.freccia_back_modifica_luogo);
        conferma = findViewById(R.id.icona_conferma_luoghi);


    }
}
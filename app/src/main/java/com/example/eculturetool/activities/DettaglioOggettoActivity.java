package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DettaglioOggettoActivity extends AppCompatActivity {

    private Connection connection = new Connection();

    private TextView nomeOggetto, descrizioneOggetto, tipologiaOggetto, zonaAppartenenza;
    private ImageView immagineOggetto;
    private FloatingActionButton cambiaImmagine, modificaOggetto;
    private Toolbar myToolbar;
    private Button eliminaOggetto;
    private Context context;

    private String idOggetto, luogoCorrente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_oggetto);

        myToolbar = (Toolbar) findViewById(R.id.toolbarOggetto);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Avvaloro context
        context = getApplicationContext();

        //Ottengo i riferimenti alle View
        nomeOggetto = findViewById(R.id.nomeOggettoDettaglio);
        descrizioneOggetto = findViewById(R.id.descrizioneOggettoDettaglio);
        tipologiaOggetto = findViewById(R.id.tipologiaOggettoDettaglio);
        zonaAppartenenza = findViewById(R.id.zonaAppartenenza);
        immagineOggetto = findViewById(R.id.immagineOggetto);
        cambiaImmagine = findViewById(R.id.change_imgObject);
        modificaOggetto = findViewById(R.id.editOggetto);
        eliminaOggetto = findViewById(R.id.eliminaOggetto);

        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        idOggetto = intent.getStringExtra(Oggetto.Keys.ID);
        luogoCorrente = intent.getStringExtra(Luogo.Keys.ID);

    }

    @Override
    protected void onStart() {
        super.onStart();

        connection.getRefOggetti().child(luogoCorrente).child(idOggetto).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Oggetto.class) != null) {
                    getSupportActionBar().setTitle(snapshot.getValue(Oggetto.class).getNome());
                    nomeOggetto.setText(snapshot.getValue(Oggetto.class).getNome());
                    descrizioneOggetto.setText(snapshot.getValue(Oggetto.class).getDescrizione());
                    Glide.with(context).load(snapshot.getValue(Oggetto.class).getUrl()).circleCrop().into(immagineOggetto);
                    //tipologiaOggetto.setText(setTipologia(snapshot.getValue(Luogo.class).getTipologia()));
                    //zonaAppartenenza.setText(snapshot.getValue(Luogo.class).getDescrizione());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eliminaOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.getRefOggetti().child(luogoCorrente).child(idOggetto).removeValue();
                finish();
            }
        });

    }
}
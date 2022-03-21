package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DettaglioLuogoActivity extends AppCompatActivity {

    private Connection connection = new Connection();

    private TextView nomeLuogo, nomeLuogoPiccolo, descrizioneLuogo, tipologiaLuogo;
    private String idLuogo;
    private Button impostaLuogoCorrente;
    private FloatingActionButton editLuogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_luogo);

        //Dichiarazione degli elementi della View
        nomeLuogo = findViewById(R.id.nomeLuogoDettaglio);
        nomeLuogoPiccolo = findViewById(R.id.nomeLuogoDettaglioPiccolo);
        descrizioneLuogo = findViewById(R.id.descrizioneDettaglio);
        tipologiaLuogo = findViewById(R.id.tipologiaDettaglio);
        impostaLuogoCorrente = findViewById(R.id.impostaLuogoCorrente);
        editLuogo = findViewById(R.id.editLuogo);


        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        idLuogo = intent.getStringExtra("LUOGO");
    }

    @Override
    protected void onStart() {
        super.onStart();

        connection.getRefLuogo().child(idLuogo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Luogo.class) != null) {
                    nomeLuogo.setText(snapshot.getValue(Luogo.class).getNome());
                    nomeLuogoPiccolo.setText(snapshot.getValue(Luogo.class).getNome());
                    descrizioneLuogo.setText(snapshot.getValue(Luogo.class).getDescrizione());
                    tipologiaLuogo.setText(setTipologia(snapshot.getValue(Luogo.class).getTipologia()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        impostaLuogoCorrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.getRefCuratore().child("luogoCorrente").setValue(idLuogo);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        editLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModificaLuogoActivity.class);
                intent.putExtra("LUOGO", idLuogo);
                startActivity(intent);
            }
        });
    }

    private String setTipologia(Tipologia tipologia) {
        String risultato = null;

        switch (tipologia) {
            case MUSEO:
                risultato = Luogo.tipologiaLuoghi.MUSEO;
                break;

            case AREA_ARCHEOLOGICA:
                risultato = Luogo.tipologiaLuoghi.AREA_ARCHEOLOGICA;
                break;

            case MOSTRA_ITINERANTE:
                risultato = Luogo.tipologiaLuoghi.MOSTRA_ITINERANTE;
                break;

            case SITO_CULTURALE:
                risultato = Luogo.tipologiaLuoghi.SITO_CULTURALE;
                break;
        }

        return risultato;
    }
}
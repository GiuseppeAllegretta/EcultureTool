package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Zona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DettaglioZonaActivity extends AppCompatActivity {
    private final Connection connection = new Connection();
    private String idZona;
    private TextView nomeZona, descrizioneZona, numeroMaxOggettiZona;
    private String luogoCorrente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_zona);

        nomeZona = findViewById(R.id.nomeZonaDettaglio);
        descrizioneZona = findViewById(R.id.descrizioneZonaDettaglio);
        numeroMaxOggettiZona = findViewById(R.id.numeroOggettiDettaglio);

        //recupero dati dall'intent

        Intent intent = getIntent();
        idZona = intent.getStringExtra("ZONA");

    }

    @Override
    protected void onStart() {
        super.onStart();

        connection.getRefCuratore().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Curatore.class) != null) {

                    //Ottengo il luogo corrente del curatore
                    luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();
                    connection.getRefZone().child(luogoCorrente).child(idZona).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(Zona.class) != null) {
                                nomeZona.setText(snapshot.getValue(Zona.class).getNome());
                                descrizioneZona.setText(snapshot.getValue(Zona.class).getDescrizione());

                                //questo da problemi, da risolvere
                                //numeroMaxOggettiZona.setText((snapshot.getValue(Zona.class).getNumeroOggetti()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
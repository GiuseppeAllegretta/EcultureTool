package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DettaglioZonaActivity extends AppCompatActivity {
    private final Connection connection = new Connection();

    private TextView nomeZona, descrizioneZona, numeroMaxOggettiZona;
    private String luogoCorrente;
    private String idZona;
    private FloatingActionButton modificaZona;
    private Button aggiungiOggettoButton;
    private Button eliminaZonaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_zona);

        nomeZona = findViewById(R.id.nomeZonaDettaglio);
        descrizioneZona = findViewById(R.id.descrizioneZonaDettaglio);
        numeroMaxOggettiZona = findViewById(R.id.numeroOggettiDettaglio);
        aggiungiOggettoButton = findViewById(R.id.aggiungiOggetto);
        eliminaZonaButton = findViewById(R.id.eliminaZona);
        modificaZona = findViewById(R.id.editZona);

        //Metodo di scroll per la textView
        descrizioneZona.setMovementMethod(new ScrollingMovementMethod());

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
                    //luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();
                    connection.getRefZone().child(luogoCorrente).child(idZona).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(Zona.class) != null) {
                                getSupportActionBar().setTitle(snapshot.getValue(Zona.class).getNome());
                                nomeZona.setText(snapshot.getValue(Zona.class).getNome());
                                descrizioneZona.setText(snapshot.getValue(Zona.class).getDescrizione());
                                numeroMaxOggettiZona.setText(String.valueOf(snapshot.getValue(Zona.class).getNumeroOggetti()));
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


        modificaZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModificaZonaActivity.class);
                intent.putExtra(Luogo.Keys.ID, luogoCorrente);
                intent.putExtra(Zona.Keys.ID, idZona);
                startActivity(intent);
            }
        });

        aggiungiOggettoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiOggettoActivity.class));
            }
        });

        eliminaZonaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_elimina_zona);

        final Button conferma = dialog.findViewById(R.id.conferma_cancellazione_zona);
        final Button rifiuto = dialog.findViewById(R.id.annulla_cancellazione_zona);

        dialog.show();

        conferma.setOnClickListener(onClickListener ->
                connection.getRefOggetti().child(luogoCorrente).child(idZona).removeValue().
                        addOnCompleteListener(onCompleteListener -> {
                            if (onCompleteListener.isSuccessful()) {
                                connection.getRefZone().child(luogoCorrente).child(idZona).removeValue();
                                dialog.dismiss();
                                finish();
                            }
                        }));

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }
}
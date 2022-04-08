package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModificaZonaActivity extends AppCompatActivity {
    private final Connection connection = new Connection();
    private String idZona;
    private EditText nomeZona, descrizioneZona, numeroMaxOggettiZona;
    private String luogoCorrente;
    private ImageView frecciaBack, conferma;
    List<Zona> zoneList = new ArrayList<>();
    private static final int MAX_OGGETTI = 10;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_zona);

        nomeZona = findViewById(R.id.nomeZonaModifica);
        descrizioneZona = findViewById(R.id.descrizioneZonaModifica);
        numeroMaxOggettiZona = findViewById(R.id.numeroOggettiModifica);
        frecciaBack = findViewById(R.id.freccia_back_modifica_zona);
        conferma = findViewById(R.id.icona_conferma_modifica_zona);
        progressBar=findViewById(R.id.progressModificaZona);


        Intent intent = getIntent();
        luogoCorrente = intent.getStringExtra(Luogo.Keys.ID);
        idZona = intent.getStringExtra(Zona.Keys.ID);

        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    zoneList = getListZoneCreate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        frecciaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editZona();
            }
        });
    }

    private void editZona() {
        int numeroMax;
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();
        String numeroMaxString = numeroMaxOggettiZona.getText().toString().trim();

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

        if (numeroMaxString.isEmpty()) {
            numeroMaxString = String.valueOf(MAX_OGGETTI);
            numeroMax = Integer.parseInt(numeroMaxString);
        } else
            numeroMax = Integer.parseInt(numeroMaxString);
        if (numeroMax > MAX_OGGETTI) {
            numeroMaxOggettiZona.setError(getResources().getString(R.string.numero_max));
            numeroMaxOggettiZona.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        connection.getRefZone().child(luogoCorrente).child(idZona).child("nome").setValue(nome);
        connection.getRefZone().child(luogoCorrente).child(idZona).child("descrizione").setValue(descrizione);
        connection.getRefZone().child(luogoCorrente).child(idZona).child("numeroOggetti").setValue(numeroMax);

        progressBar.setVisibility(View.INVISIBLE);
        finish();
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

    private List<Zona> getListZoneCreate() {

        List<Zona> zone = new ArrayList<>();

        System.out.println("Luogo corrente" + luogoCorrente);
        connection.getRefZone().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                int count = (int) snapshot.getChildrenCount();

                Zona zona;

                for (int i = 0; i < count; i++) {
                    zona=iteratore.iterator().next().getValue(Zona.class);
                    if(zona.getId().compareTo(idZona)!=0){
                        zone.add(zona);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return zone;
    }

}
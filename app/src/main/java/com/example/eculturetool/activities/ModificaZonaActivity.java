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
import com.example.eculturetool.database.DataBaseHelper;
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
    // private final Connection connection = new Connection();
    private String idZona;
    private EditText nomeZona, descrizioneZona, numeroMaxOggettiZona;
    private String luogoCorrente;
    private ImageView frecciaBack, conferma;
    List<Zona> zoneList = new ArrayList<>();
    private static final int MAX_OGGETTI = 10;
    private ProgressBar progressBar;
    private Zona z1,z2;
    private DataBaseHelper dataBaseHelper;

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


        //recupero dati dall'intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        z1 = (Zona) bundle.getSerializable("ZONE");
/*
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

*/
        dataBaseHelper = new DataBaseHelper(this);
        zoneList = dataBaseHelper.zoneQuery();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Popolamento dei campi
        nomeZona.setText(z1.getNome());
        descrizioneZona.setText(z1.getDescrizione());
        numeroMaxOggettiZona.setText(String.valueOf(z1.getNumeroOggetti()));


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

        z2= new Zona(0,nome,descrizione,Integer.parseInt(numeroMaxString),0);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        dataBaseHelper.modifica(z1,z2);
        finish();
    }

    private boolean controlloEsistenzaNomeZona(String nomeZona) {
        boolean isEsistente = false;
        //nomeZona = this.nomeZona.getText().toString();


        for (int i = 0; i < zoneList.size(); i++) {
            if (nomeZona.compareToIgnoreCase(zoneList.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }

        return isEsistente;


    }

/*
    private List<Zona> getListZoneCreate() {

        List<Zona> zone = new ArrayList<>();

        zone = dataBaseHelper.zoneQuery();

        System.out.println("Luogo corrente" + luogoCorrente);
        connection.getRefZone().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                int count = (int) snapshot.getChildrenCount();

                Zona zona;

                for (int i = 0; i < count; i++) {
                    zona=iteratore.iterator().next().getValue(Zona.class);
                   // if(zona.getId().compareTo(idZona)!=0)
                    {
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
*/
}
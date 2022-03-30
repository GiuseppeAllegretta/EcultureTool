package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AggiungiZonaActivity extends AppCompatActivity {
    private Connection connection = new Connection();

    private EditText nomeZona, descrizioneZona,numeroOggetti;
    private Button creaZona;
    private ProgressBar progressBar;
    private static final int MAX_OGGETTI = 10;
    private String luogoCorrente;
    List<Zona> zoneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_zona);
        nomeZona=findViewById(R.id.nome_zona_add);
        descrizioneZona=findViewById(R.id.descrizione_zona_add);
        numeroOggetti=findViewById(R.id.numeroOggetti_zona_add);
        progressBar=findViewById(R.id.progressAddZona);
        creaZona=findViewById(R.id.creaZona);


        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null){
                    luogoCorrente = snapshot.getValue(String.class).toString();
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
        creaZona.setOnClickListener(view -> creazioneZona());

    }

    private void creazioneZona() {
        int numeroMax;
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();
        String numeroMaxString=numeroOggetti.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeZona.setError("Il nome della zona è richiesto");
            nomeZona.requestFocus();
            return;
        }
        if (controlloEsistenzaNomeZona(nome)) {
            nomeZona.requestFocus();
            nomeZona.setError("Nome già esistente");
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneZona.setError("La descrizione è richiesta");
            descrizioneZona.requestFocus();
            return;
        }

        if(numeroMaxString.isEmpty()){
            numeroMaxString=String.valueOf(MAX_OGGETTI);
            numeroMax = Integer.parseInt(numeroMaxString);
        }else
            numeroMax=Integer.parseInt(numeroMaxString);
            if(numeroMax>MAX_OGGETTI){
                numeroOggetti.setError("Attenzione! Il numero massimo è 10");
                numeroOggetti.requestFocus();
                return;
            }

        progressBar.setVisibility(View.VISIBLE);


        //scrittura della zona sul Realtime Database
        String key=connection.getRefZone().push().getKey();
        System.out.println("KEY: " + key);
        Zona zona= new Zona(key,nome,descrizione,numeroMax);

        connection.getRefZone().child(luogoCorrente).child(key).setValue(zona);

        //CODICE ERRATO
        /*connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class)!=null){
                    String luogo=snapshot.getValue(String.class);
                    if(luogo!=null){
                        luogoCorrente=luogo;
                        if(key!=null){
                            connection.getRefZone().child(luogoCorrente).child(key).setValue(zona);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/




        progressBar.setVisibility(View.INVISIBLE);

        finish();


    }

    private boolean controlloEsistenzaNomeZona(String nomeZona) {
        boolean isEsistente = false;
        nomeZona = this.nomeZona.getText().toString();

        for (int i = 0; i < zoneList.size(); i++) {
            if (nomeZona.compareToIgnoreCase(zoneList.get(i).getNome()) == 0) {
                System.out.println("nome corrente: " + zoneList.get(i).getNome());
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
                System.out.println("count: " + count);

                Zona zona;

                for (int i = 0; i < count; i++) {
                    zone.add(iteratore.iterator().next().getValue(Zona.class));
                }
                System.out.println(zone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return zone;
    }
}
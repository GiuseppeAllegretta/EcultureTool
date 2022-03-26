package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModificaOggettoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Connection connection = new Connection();

    private EditText nomeOggetto, descrizioneOggetto;
    private Spinner tipologiaOggetto;
    private Spinner zonaAppartenenza;
    private ImageView frecciaBack, conferma;

    //Si recupera questa lista per fare in modo che l'utente non crei/modifichi un luogo con lo stesso nome di uno già creato
    private List<Oggetto> oggettiList;

    private String luogoCorrente, idOggetto;
    private TipologiaOggetto tipologia;

    //VARIABILI GESTIONE SPINNER PER LE ZONE
    private List<String> nomiZoneList = new ArrayList<>();
    private List<Zona> zoneList = new ArrayList<>();
    private ArrayAdapter<String> nomiZoneListAdapter;
    private int countZone;
    private String zonaSelezionata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_oggetto);


        nomeOggetto = findViewById(R.id.edit_name_oggetto);
        descrizioneOggetto = findViewById(R.id.editDescrizione);
        zonaAppartenenza = findViewById(R.id.spinner_zone_edit);
        tipologiaOggetto = findViewById(R.id.spinner_tipologia_oggetti_edit);
        frecciaBack = findViewById(R.id.freccia_back_modifica_oggetto);
        conferma = findViewById(R.id.icona_conferma_modifica_oggetto);

        //Ottengo i dati del luogo selezionato
        Intent intent = getIntent();
        luogoCorrente = intent.getStringExtra(Luogo.Keys.ID);
        idOggetto = intent.getStringExtra(Oggetto.Keys.ID);

        oggettiList = getListOggettiCreati();

    }

    /**
     * @return: ritorna la lista degli oggetti memorizzati su firebase in riferimento a un determinato curatore tranne quello che si è selezionato
     */
    private List<Oggetto> getListOggettiCreati() {

        List<Oggetto> oggetti = new ArrayList<>();

        connection.getRefOggetti().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                int count = (int) snapshot.getChildrenCount();
                System.out.println("count: " + count);

                Oggetto oggetto;

                for (int i = 0; i < count; i++) {
                    oggetto = iteratore.iterator().next().getValue(Oggetto.class);

                    if (oggetto.getId().compareTo(idOggetto) != 0) {
                        oggetti.add(oggetto);
                    }
                }
                System.out.println(oggetti);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return oggetti;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Setta lo spinner delle zone
        setZoneSpinner();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_oggetti, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipologiaOggetto.setAdapter(adapter);
        tipologiaOggetto.setOnItemSelectedListener(this);


        //Questa operazione va a settare nelle EditText i valori attuali dell'oggetto
        connection.getRefOggetti().child(luogoCorrente).child(idOggetto).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Oggetto.class) != null) {
                    Oggetto oggetto = snapshot.getValue(Oggetto.class);
                    nomeOggetto.setText(oggetto.getNome());
                    descrizioneOggetto.setText(oggetto.getDescrizione());
                    tipologiaOggetto.setSelection(getIndexSpinner(oggetto.getTipologiaOggetto()));
                    //TODO aggiungere la zona di appartenenza

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Istruzioni legate al click sulla freccia back
        frecciaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editOggetto();
            }
        });
    }


    private void editOggetto() {
        String nome = nomeOggetto.getText().toString().trim();
        String descrizione = descrizioneOggetto.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeOggetto.setError("Il nome dell'oggetto è richiesto");
            nomeOggetto.requestFocus();
            return;
        }

        if (controlloEsistenzaNomeOggetto(nome) == true) {
            nomeOggetto.requestFocus();
            nomeOggetto.setError("Nome già esistente");
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneOggetto.setError("La descrizione è richiesta");
            descrizioneOggetto.requestFocus();
            return;
        }

        System.out.println("tipologia: " + tipologiaOggetto);
        if (tipologiaOggetto == null) {
            tipologiaOggetto.requestFocus();
            return;
        }

        connection.getRefOggetti().child(luogoCorrente).child(idOggetto).child("nome").setValue(nome);
        connection.getRefOggetti().child(luogoCorrente).child(idOggetto).child("descrizione").setValue(descrizione);
        connection.getRefOggetti().child(luogoCorrente).child(idOggetto).child("tipologiaOggetto").setValue(tipologia);
        connection.getRefOggetti().child(luogoCorrente).child(idOggetto).child("zonaAppartenenza").setValue(zonaSelezionata);


        finish();
    }

    private int getIndexSpinner(TipologiaOggetto tipologiaOggetto) {
        int index = 0;

        switch (tipologiaOggetto) {
            case QUADRO:
                index = 0;
                break;

            case STATUA:
                index = 1;
                break;

            case SCULTURA:
                index = 2;
                break;

            case ALTRO:
                index = 3;
                break;
        }

        return index;
    }

    /**
     *
     * @param nomeOggetto nome dell'oggetto inserito nel campo della EditText
     * @return true se il nome esiste già, cioè se c'è già un altro oggetto con lo stesso nome, false altrimenti
     */
    private boolean controlloEsistenzaNomeOggetto(String nomeOggetto) {
        boolean isEsistente = false;
        nomeOggetto = this.nomeOggetto.getText().toString();

        for (int i = 0; i < oggettiList.size(); i++) {
            if (nomeOggetto.compareToIgnoreCase(oggettiList.get(i).getNome()) == 0) {
                //System.out.println("nome corrente: " + luoghiList.get(i).getNome());
                isEsistente = true;
            }
        }

        return isEsistente;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String item = adapterView.getItemAtPosition(i).toString();
        System.out.println("item: " + item);

        switch (item) {
            case Oggetto.KeysTipologiaOggetto.QUADRO:
                tipologia = TipologiaOggetto.QUADRO;
                break;

            case Oggetto.KeysTipologiaOggetto.STATUA:
                tipologia = TipologiaOggetto.STATUA;
                break;

            case Oggetto.KeysTipologiaOggetto.SCULTURA:
                tipologia = TipologiaOggetto.SCULTURA;
                break;

            case Oggetto.KeysTipologiaOggetto.ALTRO:
                tipologia = TipologiaOggetto.ALTRO;
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * Metodo per settare l'elenco delle zone nello spinner relativo
     */
    private void setZoneSpinner() {

        System.out.println("Luogo corrente: " + luogoCorrente);

        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null) {
                    luogoCorrente = snapshot.getValue(String.class).toString();

                    connection.getRefZone().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue(Zona.class) != null){
                                Iterable<DataSnapshot> iteratoreZone = snapshot.getChildren();
                                countZone = (int) snapshot.getChildrenCount();
                                System.out.println("countZone: " + countZone);

                                zoneList.clear(); //pulisce la lista prima di riempirla
                                //Avvalora zoneList con tutte le zone prese da db
                                for(int i = 0; i < countZone; i++){
                                    zoneList.add(iteratoreZone.iterator().next().getValue(Zona.class));
                                }

                                nomiZoneList.clear(); //pulisce la lista prima di riempirla
                                //Avvalora nomiZoneList con i nomi di ogni singola zona
                                for(int i = 0; i < countZone; i++){
                                    nomiZoneList.add(zoneList.get(i).getNome());
                                }

                                nomiZoneListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, nomiZoneList);
                                zonaAppartenenza.setAdapter(nomiZoneListAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                zonaAppartenenza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        zonaSelezionata = nomiZoneList.get(i);
                        System.out.println("Zona selezionata: " + zonaSelezionata);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
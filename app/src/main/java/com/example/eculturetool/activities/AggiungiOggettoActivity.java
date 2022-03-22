package com.example.eculturetool.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AggiungiOggettoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Connection connection = new Connection();

    private EditText nomeOggetto, descrizioneOggetto;
    private Spinner tipologiaOggetto;
    private Button creaOggetto;
    private ProgressBar progressBar;

    private TipologiaOggetto tipologia;

    //Si recupera questa lista per fare in modo che l'utente non crei un luogo con lo stesso nome di quello precedente
    List<Oggetto> oggettiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_oggetti);

        nomeOggetto = findViewById(R.id.nome_oggetto_add);
        descrizioneOggetto = findViewById(R.id.descrizione_oggetto_add);
        tipologiaOggetto = findViewById(R.id.spinner_tipologia_oggetto_add);
        creaOggetto = findViewById(R.id.creaOggetto);
        progressBar = findViewById(R.id.progressAddOggetto);

        oggettiList = getListOggettiCreati();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_oggetti, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipologiaOggetto.setAdapter(adapter);
        tipologiaOggetto.setOnItemSelectedListener(this);
        creaOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creazioneOggetto();
            }
        });
    }

    private void creazioneOggetto() {

        String nome = nomeOggetto.getText().toString().trim();
        String descrizione = descrizioneOggetto.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeOggetto.setError("Il nome dell'oggetto è richiesto");
            nomeOggetto.requestFocus();
            return;
        }

        if (controlloEsistenzaNomeOggetto(nome)) {
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


        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        //Scrittura del luogo sul Realtime Database
        String key = connection.getRefOggetti().push().getKey();
        System.out.println("KEY: " + key);
        Oggetto oggetto = new Oggetto(key, nome, descrizione, "prova");
        connection.getRefOggetti().child(key).child(connection.getRefCuratore().child("luogoCorrente").toString()).setValue(oggetto);

        //La progressbar diventa visibile
        progressBar.setVisibility(View.INVISIBLE);

        finish();

    }


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


    /**
     * @return: ritorna la lista dei luighi memorizzati su firebase in riferimento a un determinato curatore
     */
    private List getListOggettiCreati() {
        List<Oggetto> oggetti = new ArrayList<>();

        connection.getRefLuogo().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                int count = (int) snapshot.getChildrenCount();
                System.out.println("count: " + count);

                for (int i = 0; i < count; i++) {
                    oggettiList.add(iteratore.iterator().next().getValue(Oggetto.class));
                    System.out.println(oggettiList.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return oggetti;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        System.out.println("entra in onItemSelected");
        String item = adapterView.getItemAtPosition(i).toString();
        System.out.println("item: " + item);

        switch (item) {
            case "Quadro":
                tipologia = TipologiaOggetto.QUADRO;
                break;

            case "Statua":
                tipologia = TipologiaOggetto.STATUA;
                break;

            case "Scultura":
                tipologia = TipologiaOggetto.SCULTURA;
                break;

            case "Sito culturale":
                tipologia = TipologiaOggetto.ALTRO;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
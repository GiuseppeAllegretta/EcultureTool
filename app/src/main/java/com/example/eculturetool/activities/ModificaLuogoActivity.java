package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModificaLuogoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Connection connection = new Connection();

    private EditText nomeLuogo, descrizioneLuogo;
    private Spinner tipologiaLuogo;
    private ImageView frecciaBack, conferma;
    private String idLuogo;

    //Si recupera questa lista per fare in modo che l'utente non crei/modifichi un luogo con lo stesso nome di uno già creato
    private List<Luogo> luoghiList;
    private Tipologia tipologia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_luogo);

        nomeLuogo = findViewById(R.id.edit_name_luogo);
        descrizioneLuogo = findViewById(R.id.edit_descrizione_luogo);
        tipologiaLuogo = findViewById(R.id.spinner_tipologia_luoghi_modifica);
        frecciaBack = findViewById(R.id.freccia_back_modifica_luogo);
        conferma = findViewById(R.id.icona_conferma_luoghi);

        //Ottengo i dati del luogo selezionato
        Intent intent = getIntent();
        idLuogo = intent.getStringExtra("LUOGO");
        System.out.println("Luogo id:" + idLuogo);

        luoghiList = getListLuoghiCreati();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_luoghi, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipologiaLuogo.setAdapter(adapter);
        tipologiaLuogo.setOnItemSelectedListener(this);

        connection.getRefLuogo().child(idLuogo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Luogo.class) != null) {
                    Luogo luogo = snapshot.getValue(Luogo.class);
                    nomeLuogo.setText(luogo.getNome());
                    descrizioneLuogo.setText(luogo.getDescrizione());
                    tipologiaLuogo.setSelection(getIndexSpinner(luogo.getTipologia()));
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
                editLuogo();
            }
        });

    }


    private int getIndexSpinner(Tipologia tipologia) {
        int index = 0;

        switch (tipologia) {
            case MUSEO:
                index = 0;
                break;

            case AREA_ARCHEOLOGICA:
                index = 1;
                break;

            case MOSTRA_ITINERANTE:
                index = 2;
                break;

            case SITO_CULTURALE:
                index = 3;
                break;
        }

        return index;
    }


    private void editLuogo() {

        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();


        if (nome.isEmpty()) {
            nomeLuogo.setError("Il nome del luogo è richiesto");
            nomeLuogo.requestFocus();
            return;
        }

        if (controlloEsistenzaNomeLuogo(nome) == true) {
            nomeLuogo.requestFocus();
            nomeLuogo.setError("Nome già esistente");
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneLuogo.setError("La descrizione è richiesta");
            descrizioneLuogo.requestFocus();
            return;
        }

        System.out.println("tipologia: " + tipologiaLuogo);
        if (tipologiaLuogo == null) {
            tipologiaLuogo.requestFocus();
            return;
        }

        connection.getRefLuogo().child(idLuogo).child("nome").setValue(nome);
        connection.getRefLuogo().child(idLuogo).child("descrizione").setValue(descrizione);
        connection.getRefLuogo().child(idLuogo).child("tipologia").setValue(tipologia);

        finish();
    }

    /**
     * @return: ritorna la lista dei luighi memorizzati su firebase in riferimento a un determinato curatore
     */
    private List getListLuoghiCreati() {
        List<Luogo> luoghi = new ArrayList<>();

        connection.getRefLuogo().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                int count = (int) snapshot.getChildrenCount();
                System.out.println("count: " + count);

                Luogo luogo;

                for (int i = 0; i < count; i++) {
                    luogo = iteratore.iterator().next().getValue(Luogo.class);

                    if (luogo.getId().compareTo(idLuogo) != 0) {
                        luoghiList.add(luogo);
                    }
                }
                System.out.println(luoghiList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return luoghi;
    }

    private boolean controlloEsistenzaNomeLuogo(String nomeLuogo) {
        boolean isEsistente = false;
        nomeLuogo = this.nomeLuogo.getText().toString();

        for (int i = 0; i < luoghiList.size(); i++) {
            if (nomeLuogo.compareToIgnoreCase(luoghiList.get(i).getNome()) == 0) {
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
            case "Museo":
                tipologia = Tipologia.MUSEO;
                break;

            case "Area archeologica":
                tipologia = Tipologia.AREA_ARCHEOLOGICA;
                break;

            case "Mostra itinerante":
                tipologia = Tipologia.MOSTRA_ITINERANTE;
                break;

            case "Sito culturale":
                tipologia = Tipologia.SITO_CULTURALE;
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
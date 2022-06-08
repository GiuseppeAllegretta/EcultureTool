package com.example.eculturetool.activities.luoghi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;

import java.util.List;

public class ModificaLuogoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DataBaseHelper dataBaseHelper;
    private int idLuogo;                         //id del luogo selezionato nella recyclerview prededente e ottenuto tramite intent
    private Luogo luogo;
    private EditText nomeLuogo, descrizioneLuogo;
    private Spinner tipologiaLuogo;
    private ImageView frecciaBack, conferma;
    private List<Luogo> luoghiList;
    private Tipologia tipologia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_luogo);

        //Dichiarazione degli elementi della View
        nomeLuogo = findViewById(R.id.edit_name_luogo);
        descrizioneLuogo = findViewById(R.id.edit_descrizione_luogo);
        tipologiaLuogo = findViewById(R.id.spinner_tipologia_luoghi_modifica);
        frecciaBack = findViewById(R.id.freccia_back_modifica_luogo);
        conferma = findViewById(R.id.icona_conferma_luoghi);

        //Ottengo i dati del luogo selezionato dall'intent
        Intent intent = getIntent();
        idLuogo = intent.getIntExtra(Luogo.Keys.ID, 0);

        dataBaseHelper = new DataBaseHelper(this);
        luoghiList = getListLuoghiCreati();
        luogo = dataBaseHelper.getLuogoById(idLuogo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_luoghi, android.R.layout.simple_spinner_item);
        //Azione da eseguire quando si clicca la freccia di navigazione
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applica l'adapter allo spinner
        tipologiaLuogo.setAdapter(adapter);
        tipologiaLuogo.setOnItemSelectedListener(this);

        //metodo che si occupa di popolare i capi nella sezione di modifica
        popolaCampi();

        //operazioni da eseguire quando si clicca sulla freccia back
        frecciaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //operazioni da eseguire quando si clicca sulla pulsante conferma
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLuogo();
            }
        });

    }

    /**
     * Metodo che va a riempire le editText con i valori recuperati dall'intent
     */
    private void popolaCampi() {
        if(luogo != null){
            nomeLuogo.setText(luogo.getNome());
            descrizioneLuogo.setText(luogo.getDescrizione());
            tipologiaLuogo.setSelection(getIndexSpinner(luogo.getTipologia()));
        }
    }


    /**
     * Metodo che restituisce l'indice sulla base della scelta effettuata dall'utente
     * @param tipologia
     * @return
     */
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
        //Recupero i dati dalla editText
        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        //operazioni da eseguire nel caso in cui la editText del nome è vuota
        if (nome.isEmpty()) {
            nomeLuogo.setError(getResources().getString(R.string.nome_luogo_richiesto));
            nomeLuogo.requestFocus();
            return;
        }

        //operazioni da eseguire nel caso in cui  il nome dato al luogo esiste
        if (controlloEsistenzaNomeLuogo(nome)) {
            nomeLuogo.requestFocus();
            nomeLuogo.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        //operazioni da eseguire nel caso in cui la editText della descrizione è vuota
        if (descrizione.isEmpty()) {
            descrizioneLuogo.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneLuogo.requestFocus();
            return;
        }

        //operazioni da eseguire nel caso in cui la scelta effettuata nello spinner è nulla
        if (tipologiaLuogo == null) {
            tipologiaLuogo.requestFocus();
            return;
        }

        //operazioni da eseguire nel caso in cui l'aggiornamento dei dati nel DB sia andato a buon fine
        if(dataBaseHelper.updateLuogo(idLuogo, nome, descrizione, tipologia)){
            Toast.makeText(this, getResources().getString(R.string.aggiornati_dati), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getResources().getString(R.string.no_aggiornati_dati), Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    /**
     * @return: ritorna la lista dei luighi memorizzati su firebase in riferimento a un determinato curatore
     */
    private List getListLuoghiCreati() {
        List<Luogo> returnList;

        returnList = dataBaseHelper.getLuoghi();

        //rimuove il luogo selezionato dalla lista dei luoghi per far si che l'utente possa dare sempre lo stesso nome al luogo che si sta modificando
        //non avrebbe senso impedire all'utente di dare lo stesso nome al luogo che si sta modificando
        for(int i = 0; i < returnList.size(); i++){
            if(returnList.get(i).getId() == idLuogo){
                returnList.remove(i);
            }
        }

        return returnList;
    }



    /**
     * Metodo che effettua un controllo sul nome che l'utente ha dato al luogo
     * @param nomeLuogo nome da confrontare
     * @return true se il nome dato al luogo dall'utente già esiste, false altrimenti
     */
    private boolean controlloEsistenzaNomeLuogo(String nomeLuogo) {
        boolean isEsistente = false;
        nomeLuogo = this.nomeLuogo.getText().toString();

        for (int i = 0; i < luoghiList.size(); i++) {
            if (nomeLuogo.compareToIgnoreCase(luoghiList.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }

        return isEsistente;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

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
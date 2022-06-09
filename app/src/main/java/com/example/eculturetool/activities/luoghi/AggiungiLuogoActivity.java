package com.example.eculturetool.activities.luoghi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;

import java.util.List;

public class AggiungiLuogoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DataBaseHelper dataBaseHelper;
    private EditText nomeLuogo, descrizioneLuogo;
    private Spinner tipologiaLuogo;
    private Button creaLuogo;
    private ProgressBar progressBar;
    private Tipologia tipologia;

    //lista di oggetti di tipo luogo che conterrà tutti i luoghi precedentemente creati dall'utente
    private List<Luogo> luoghiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_luogo);

        //Qui si prendono i riferimenti agli elementi del layout
        nomeLuogo = findViewById(R.id.nome_luogo_add);
        descrizioneLuogo = findViewById(R.id.descrizione_luogo_add);
        tipologiaLuogo = findViewById(R.id.spinner_tipologia_luoghi_add);
        creaLuogo = findViewById(R.id.creaLuogo);
        progressBar = findViewById(R.id.progressAddLuogo);
        dataBaseHelper = new DataBaseHelper(this);

        //si prendono tutti i luoghi e si memorizzano in questa lists
        luoghiList = dataBaseHelper.getLuoghi();

        //operazioni da eseguire nel caso in cui si clicca sul pulsante di creazione del luogo
        creaLuogo.setOnClickListener(view -> {
            creazioneLuogo();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Crea un ArrayAdapter usando un array di stringhe e uno spinner predefinito
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_luoghi, android.R.layout.simple_spinner_item);
        // Specificare il layout da utilizzare quando viene visualizzato l'elenco delle scelte
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applico l'adapter allo spinner
        tipologiaLuogo.setAdapter(adapter);
        tipologiaLuogo.setOnItemSelectedListener(this);

    }

    /**
     * Metodo void che si occupa di tutta la logica di creazione del luogo
     */
    private void creazioneLuogo() {
        //Delego la gestione della progress bar ad un apposito handler
        Handler handler = new Handler(getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                //Messaggio che rende invisibile la progressBar
                if (message.what == 1) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                //La progressbar diventa visibile
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        //prendo i valori che l'utente ha scritto nelle due editText
        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        //operazioni da eseguire nel caso in cui la editText del nome è vuota
        if (nome.isEmpty()) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            nomeLuogo.requestFocus();
            nomeLuogo.setError(getResources().getString(R.string.nome_luogo_richiesto));
            return;
        }

        //operazioni da eseguire nel caso in cui  il nome dato al luogo esista
        if (controlloEsistenzaNomeLuogo()) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            nomeLuogo.requestFocus();
            nomeLuogo.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        //operazioni da eseguire nel caso in cui la editText della descrizione è vuota
        if (descrizione.isEmpty()) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            descrizioneLuogo.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneLuogo.requestFocus();
            return;
        }

        //operazioni da eseguire nel caso in cui la scelta effettuata nello spinner è nulla
        if (tipologiaLuogo == null) {
            //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            tipologiaLuogo.requestFocus();
            return;
        }

        //creazione del luogo
        Luogo luogo = new Luogo(nome, descrizione, tipologia, DataBaseHelper.getEmailCuratore());

        //caso in cui la creazione del luogo va a buon fine nel DB
        if(dataBaseHelper.addLuogo(luogo)){
            finish();
        }else {
            Toast.makeText(this, getResources().getString(R.string.db_errore_scrittura), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Metodo che effettua un controllo sul nome che l'utente ha dato al luogo
     * @return true se il nome dato al luogo dall'utente già esiste, false altrimenti
     */
    private boolean controlloEsistenzaNomeLuogo() {
        boolean isEsistente = false;
        String nomeLuogo = this.nomeLuogo.getText().toString();

        //si compara il nome recuperato dalla edit text e che ha dato l'utente con i nomi degli oggetti che sono stati già scritti nel DB
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
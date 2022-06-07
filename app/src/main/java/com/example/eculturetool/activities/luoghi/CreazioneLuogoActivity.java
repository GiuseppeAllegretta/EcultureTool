package com.example.eculturetool.activities.luoghi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.LoginActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;


public class CreazioneLuogoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DataBaseHelper dataBaseHelper;
    private EditText nomeLuogo, descrizioneLuogo;
    private Spinner tipologiaLuogo;
    private Button registrazione;
    private ProgressBar progressBar;

    /**
     * Variabile che andrà a contenere l'oggetto curatore che viene recuperato dal Bundle attraverso un intent
     */
    private Curatore curatore;
    private Tipologia tipologia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_museo);

        //Acquisizione riferimenti view nell'acitivty
        nomeLuogo = findViewById(R.id.nome_luogo);
        descrizioneLuogo = findViewById(R.id.descrizione_luogo);
        tipologiaLuogo = findViewById(R.id.spinner_tipologia_luoghi);
        registrazione = findViewById(R.id.registrazione);
        progressBar = findViewById(R.id.progressLuogo);
        dataBaseHelper = new DataBaseHelper(this);

        //Operazioni che prevedono il recupero dei dati dall'intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        curatore = (Curatore) bundle.getSerializable(Curatore.Keys.CURATORE_KEY);
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

        //operazioni da eseguire quando l'utente effettua il click sul pulsante di registrazione
        registrazione.setOnClickListener(view -> registrazione());
    }


    /**
     * Metodo void contiene la logica di esecuzione della registrazione
     */
    private void registrazione() {
        //Recupero dai dati dalle EditText della view
        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        //operazioni da eseguire nel caso in cui la editText del nome è vuota
        if (nome.isEmpty()) {
            nomeLuogo.setError(getResources().getString(R.string.nome_luogo_richiesto));
            nomeLuogo.requestFocus();
            return;
        }

        //operazioni da eseguire nel caso in cui la editText della descrizione è vuota
        if (descrizione.isEmpty()) {
            descrizioneLuogo.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneLuogo.requestFocus();
            return;
        }

        //operazioni da eseguire nel caso in cui la scelta effettuata nello spinner è nulla
        if (tipologia == null) {
            tipologiaLuogo.requestFocus();
            return;
        }

        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        Luogo luogo = new Luogo(nome, descrizione, tipologia, curatore.getEmail());

        //operazioni da eseguire nel caso in cui la scrittura del luogo nel DB sia andata a buon fine
        if(dataBaseHelper.addLuogo(luogo)){
            //recupero dell'Id del luogo dal DB
            int idLuogoCreato = dataBaseHelper.getFirstLuogoCorrente(curatore.getEmail());
            //setta il luogo appena recuperato come luogo corrente
            curatore.setLuogoCorrente(idLuogoCreato);

            //operazioni da eseguire nel caso in cui la scrittura su DB sia andata a buon fine
            if(dataBaseHelper.addCuratore(curatore)){
                Toast.makeText(CreazioneLuogoActivity.this, getResources().getString(R.string.registrazione_completata), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreazioneLuogoActivity.this, LoginActivity.class));
                progressBar.setVisibility(View.INVISIBLE);
            }else{
                Toast.makeText(CreazioneLuogoActivity.this, getResources().getString(R.string.registrazione_fallita), Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(CreazioneLuogoActivity.this, getResources().getString(R.string.registrazione_fallita), Toast.LENGTH_SHORT).show();
        }
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
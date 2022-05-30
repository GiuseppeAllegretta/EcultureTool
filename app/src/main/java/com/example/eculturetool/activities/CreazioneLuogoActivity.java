package com.example.eculturetool.activities;

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
        registrazione.setOnClickListener(view -> registrazione());

    }

    private void registrazione() {
        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeLuogo.setError(getResources().getString(R.string.nome_luogo_richiesto));
            nomeLuogo.requestFocus();
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneLuogo.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneLuogo.requestFocus();
            return;
        }

        if (tipologia == null) {
            tipologiaLuogo.requestFocus();
            return;
        }

        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        Luogo luogo = new Luogo(nome, descrizione, tipologia, curatore.getEmail());

        if(dataBaseHelper.addLuogo(luogo)){
            int idLuogoCreato = dataBaseHelper.getFirstLuogoCorrente(curatore.getEmail());
            curatore.setLuogoCorrente(idLuogoCreato);

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
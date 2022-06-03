package com.example.eculturetool.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

    //Si recupera questa lista per fare in modo che l'utente non crei un luogo con lo stesso nome di quello precedente
    private List<Luogo> luoghiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_luogo);

        nomeLuogo = findViewById(R.id.nome_luogo_add);
        descrizioneLuogo = findViewById(R.id.descrizione_luogo_add);
        tipologiaLuogo = findViewById(R.id.spinner_tipologia_luoghi_add);
        creaLuogo = findViewById(R.id.creaLuogo);
        progressBar = findViewById(R.id.progressAddLuogo);
        dataBaseHelper = new DataBaseHelper(this);

        luoghiList = dataBaseHelper.getLuoghi();
        creaLuogo.setOnClickListener(view -> {
            creazioneLuogo();
            finish();
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

    private void creazioneLuogo() {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //La progressbar diventa visibile
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        if (nome.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            nomeLuogo.requestFocus();
            nomeLuogo.setError(getResources().getString(R.string.nome_luogo_richiesto));
            return;
        }

        if (controlloEsistenzaNomeLuogo()) {
            progressBar.setVisibility(View.INVISIBLE);
            nomeLuogo.requestFocus();
            nomeLuogo.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        if (descrizione.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            descrizioneLuogo.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneLuogo.requestFocus();
            return;
        }

        if (tipologiaLuogo == null) {
            progressBar.setVisibility(View.INVISIBLE);
            tipologiaLuogo.requestFocus();
            return;
        }

        Luogo luogo = new Luogo(nome, descrizione, tipologia, DataBaseHelper.getEmailCuratore());
        dataBaseHelper.addLuogo(luogo);
    }


    private boolean controlloEsistenzaNomeLuogo() {
        boolean isEsistente = false;
        String nomeLuogo = this.nomeLuogo.getText().toString();

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
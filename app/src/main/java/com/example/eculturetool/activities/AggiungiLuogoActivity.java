package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AggiungiLuogoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Connection connection = new Connection();

    private EditText nomeLuogo, descrizioneLuogo;
    private Spinner tipologiaLuogo;
    private Button creaLuogo;
    private ProgressBar progressBar;

    private Tipologia tipologia;

    //Si recupera questa lista per fare in modo che l'utente non crei un luogo con lo stesso nome di quello precedente
    List<Luogo> luoghiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_luogo);

        nomeLuogo = findViewById(R.id.nome_luogo_add);
        descrizioneLuogo = findViewById(R.id.descrizione_luogo_add);
        tipologiaLuogo = findViewById(R.id.spinner_tipologia_luoghi_add);
        creaLuogo = findViewById(R.id.creaLuogo);
        progressBar = findViewById(R.id.progressAddLuogo);

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
        creaLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creazioneLuogo();
            }
        });
    }

    private void creazioneLuogo() {

        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeLuogo.setError("Il nome del luogo è richiesto");
            nomeLuogo.requestFocus();
            return;
        }

        if(controlloEsistenzaNomeLuogo(nome) == true){
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


        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        //Scrittura del luogo sul Realtime Database
        String key = connection.getRefLuogo().push().getKey();
        System.out.println("KEY: "+ key);
        Luogo luogo = new Luogo(nome, descrizione, tipologia, key);


        connection.getRefLuogo().child(key).setValue(luogo);

        //La progressbar diventa visibile
        progressBar.setVisibility(View.INVISIBLE);

        finish();

    }


    private boolean controlloEsistenzaNomeLuogo(String nomeLuogo){
        boolean isEsistente = false;
        nomeLuogo = this.nomeLuogo.getText().toString();

        for(int i = 0; i < luoghiList.size(); i++){
            if(nomeLuogo.compareToIgnoreCase(luoghiList.get(i).getNome()) == 0){
                //System.out.println("nome corrente: " + luoghiList.get(i).getNome());
                isEsistente = true;
            }
        }

        return isEsistente;
    }


    /**
     *
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

                for(int i = 0; i < count; i++){
                    luoghiList.add(iteratore.iterator().next().getValue(Luogo.class));
                    System.out.println(luoghiList.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return luoghi;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        System.out.println("entra in onItemSelected");
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
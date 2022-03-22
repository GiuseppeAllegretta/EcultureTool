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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.TipologiaLuogo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class CreazioneMuseoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Connection connection = new Connection();

    private EditText nomeLuogo, descrizioneLuogo;
    private Spinner tipologiaLuogo;
    private Button registrazione;
    private ProgressBar progressBar;

    private Curatore curatore;
    private String password;
    private Tipologia tipologia;

    final static String SELEZIONA_NUOVO_ELEMENTO = "seleziona un elemento";

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

        curatore = new Curatore();
        Intent intent = getIntent();


        Bundle bundle = intent.getExtras();
        curatore = (Curatore) bundle.getSerializable(Curatore.Keys.CURATORE_KEY);
        password = bundle.getString(Curatore.Keys.PASSWORD_KEY);

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
        registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrazione();
            }
        });

    }

    private void registrazione() {

        String nome = nomeLuogo.getText().toString().trim();
        String descrizione = descrizioneLuogo.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeLuogo.setError("Il nome del luogo è richiesto");
            nomeLuogo.requestFocus();
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneLuogo.setError("La descrizione è richiesta");
            descrizioneLuogo.requestFocus();
            return;
        }

        System.out.println("tipologia: " + tipologia);
        if (tipologia == null) {
            tipologiaLuogo.requestFocus();
            return;
        }


        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        connection.getAuth().createUserWithEmailAndPassword(curatore.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = connection.getAuth().getCurrentUser();
                            System.out.println("UID: " + user.getUid());

                            //Scrittura del curatore sul Realtime Database
                            //connection.getRefCuratore().setValue(curatore);
                            connection.getDatabaseReference().child("curatori").child(user.getUid()).setValue(curatore);


                            //Scrittura del luogo sul Realtime Database
                            String key = connection.getRefLuogo().push().getKey();
                            System.out.println("KEY: " + key);
                            Luogo luogo = new Luogo(nome, descrizione, tipologia, key);


                            connection.getRefLuogo().child(key).setValue(luogo);

                            //Settaggio del luogo appena creato come luogo corrente
                            connection.getRefCuratore().child("luogoCorrente").setValue(key);


                            startActivity(new Intent(CreazioneMuseoActivity.this, LoginActivity.class));


                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                System.out.println("email inviata");
                                            }
                                        }
                                    });

                            Toast.makeText(CreazioneMuseoActivity.this, "Registrazione completata. Inviata email di verifica", Toast.LENGTH_SHORT).show();

                            //La progressBar diventa invisibile
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {

                            // If sign in fails, display a message to the user.
                            Toast.makeText(CreazioneMuseoActivity.this, "Registrazione Fallita", Toast.LENGTH_SHORT).show();

                            //La progressBar diventa invisibile
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });


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
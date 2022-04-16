package com.example.eculturetool.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DettaglioLuogoActivity extends AppCompatActivity {

    private final Connection connection = new Connection();

    private TextView nomeLuogo, descrizioneLuogo, tipologiaLuogo;
    private int idLuogo;
    private Button impostaLuogoCorrente;
    private Button eliminaLuogo;
    private FloatingActionButton editLuogo;

    private ValueEventListener mListenerDeleteLuogo;
    private int numeroLuoghi;
    private int luogoCorrente;
    private static final int MIN_LUOGHI = 1;
    private List<Luogo> luoghiList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_luogo);

        //Dichiarazione degli elementi della View
        nomeLuogo = findViewById(R.id.nomeLuogoDettaglioPiccolo);
        descrizioneLuogo = findViewById(R.id.descrizioneDettaglio);
        tipologiaLuogo = findViewById(R.id.tipologiaDettaglio);
        impostaLuogoCorrente = findViewById(R.id.impostaLuogoCorrente);
        editLuogo = findViewById(R.id.editLuogo);
        eliminaLuogo = findViewById(R.id.eliminaLuogo);

        //Metodo di scroll per la textView
        descrizioneLuogo.setMovementMethod(new ScrollingMovementMethod());


        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        //idLuogo = intent.getStringExtra("LUOGO");

        searchLuogoCorrente();

        mListenerDeleteLuogo = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numeroLuoghi = (int) snapshot.getChildrenCount();
                System.out.println("numeroLuoghi: " + numeroLuoghi);

                Iterable<DataSnapshot> iteratore = snapshot.getChildren();

                for(int i = 0; i < numeroLuoghi; i++){
                    luoghiList.add(iteratore.iterator().next().getValue(Luogo.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        connection.getRefLuoghi().addValueEventListener(mListenerDeleteLuogo);

    }

    @Override
    protected void onStart() {
        super.onStart();

        /*connection.getRefLuoghi().child(idLuogo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Luogo.class) != null) {
                    getSupportActionBar().setTitle(snapshot.getValue(Luogo.class).getNome());
                    nomeLuogo.setText(snapshot.getValue(Luogo.class).getNome());
                    descrizioneLuogo.setText(snapshot.getValue(Luogo.class).getDescrizione());
                    tipologiaLuogo.setText(setTipologia(snapshot.getValue(Luogo.class).getTipologia()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        impostaLuogoCorrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.getRefCuratore().child("luogoCorrente").setValue(idLuogo);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        editLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModificaLuogoActivity.class);
                intent.putExtra("LUOGO", idLuogo);
                startActivity(intent);
            }
        });

        //eliminaLuogo();

    }

    private void searchLuogoCorrente() {
        //Ricerca del luogo corrente
        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null){
                    //luogoCorrente = snapshot.getValue(String.class).toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String setTipologia(Tipologia tipologia) {
        String risultato = null;

        switch (tipologia) {
            case MUSEO:
                risultato = Luogo.tipologiaLuoghi.MUSEO;
                break;

            case AREA_ARCHEOLOGICA:
                risultato = Luogo.tipologiaLuoghi.AREA_ARCHEOLOGICA;
                break;

            case MOSTRA_ITINERANTE:
                risultato = Luogo.tipologiaLuoghi.MOSTRA_ITINERANTE;
                break;

            case SITO_CULTURALE:
                risultato = Luogo.tipologiaLuoghi.SITO_CULTURALE;
                break;
        }

        return risultato;
    }


    /*private void eliminaLuogo() {

        eliminaLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numeroLuoghi == MIN_LUOGHI) {
                    System.out.println("Primo if numero luoghi: " + numeroLuoghi);
                    Toast.makeText(DettaglioLuogoActivity.this, getResources().getString(R.string.min_luoghi), Toast.LENGTH_LONG).show();
                    //TODO bisogna far uscire un dialog che indica che non è possibile eliminare il luogo in quanto ci deve essere almeno un luogo attivo
                } else {
                    if (idLuogo == luogoCorrente) {

                        int luogoSelezionato;
                        for (int i = 0; i < numeroLuoghi; i++) {
                            luogoSelezionato = luoghiList.get(i).getId();
                            if (idLuogo == luogoSelezionato) {
                                System.out.println("if del break: ");
                                connection.getRefCuratore().child("luogoCorrente").setValue(luogoSelezionato);
                                connection.getRefOggetti().child(idLuogo).removeValue();
                                connection.getRefZone().child(idLuogo).removeValue();
                                connection.getRefLuoghi().child(idLuogo).removeValue();
                                break;
                            }
                        }
                    } else {
                        connection.getRefOggetti().child(idLuogo).removeValue();
                        connection.getRefZone().child(idLuogo).removeValue();
                        connection.getRefLuoghi().child(idLuogo).removeValue();
                    }
                    showDialog();
                }

            }
        });

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.getRefLuoghi().removeEventListener(mListenerDeleteLuogo);

    }

    @Override
    protected void onResume() {
        super.onResume();
        connection.getRefLuoghi().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numeroLuoghi = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.avviso))
                .setMessage(getResources().getString(R.string.avviso_luoghi_zone))
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        alert.create().show();
    }
}
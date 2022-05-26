package com.example.eculturetool.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DettaglioZonaActivity extends AppCompatActivity {

    private String emailOspite = "admin@gmail.com"; //email dell'account ospite

    private TextView nomeZona, descrizioneZona;
    private String luogoCorrente;
    private String idZona;
    private FloatingActionButton modificaZona;
    private Button aggiungiOggettoButton;
    private Button eliminaZonaButton;
    private Zona z, zM ;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_zona);

        nomeZona = findViewById(R.id.nomeZonaDettaglio);
        descrizioneZona = findViewById(R.id.descrizioneZonaDettaglio);
        aggiungiOggettoButton = findViewById(R.id.aggiungiOggetto);
        eliminaZonaButton = findViewById(R.id.eliminaZona);
        modificaZona = findViewById(R.id.editZona);

        //Metodo di scroll per la textView
        descrizioneZona.setMovementMethod(new ScrollingMovementMethod());

        //recupero dati dall'intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        z= (Zona) bundle.getSerializable("ZONE");

        nomeZona.setText(z.getNome().toString());
        descrizioneZona.setText(z.getDescrizione().toString());
        getSupportActionBar().setTitle(z.getNome());


        dataBaseHelper = new DataBaseHelper(this);

        nascondiView();
    }

    /**
     * Questo metodo consente di nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite
     */
    private void nascondiView() {
        dataBaseHelper = new DataBaseHelper(this);

        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        if(emailCuratore.compareTo(emailOspite) == 0){
            aggiungiOggettoButton.setVisibility(View.INVISIBLE);
            eliminaZonaButton.setVisibility(View.INVISIBLE);
            modificaZona.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        zM = dataBaseHelper.recuperoZonaModificata(z.getId());
        nomeZona.setText(zM.getNome().toString());
        descrizioneZona.setText(zM.getDescrizione().toString());

    }

    @Override
    protected void onStart() {
        super.onStart();

        modificaZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModificaZonaActivity.class);

                Bundle b = new Bundle();
                b.putSerializable("ZONE",z);
                intent.putExtras(b);
                startActivityForResult(intent,123);
                //TODO deprecation, che bella sensation
            }
        });



        aggiungiOggettoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiOggettoActivity.class));
            }
        });

        eliminaZonaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

    }



    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);

        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText(getResources().getString(R.string.cancella_zona));
        TextView descrizione_tv = layout.findViewById(R.id.descrizione_dialog);
        descrizione_tv.setText(getResources().getString(R.string.NB_zona));

        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.rimuoviZona(z);
                onBackPressed();
            }
        });
        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }


}
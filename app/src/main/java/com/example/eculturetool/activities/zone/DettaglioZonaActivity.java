package com.example.eculturetool.activities.zone;

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
import com.example.eculturetool.activities.oggetti.AggiungiOggettoActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class DettaglioZonaActivity extends AppCompatActivity {

    private TextView nomeZona, descrizioneZona;
    private FloatingActionButton modificaZona;
    private Button aggiungiOggettoButton;
    private Button eliminaZonaButton;
    private Zona z;
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

        nomeZona.setText(z.getNome());
        descrizioneZona.setText(z.getDescrizione());
        Objects.requireNonNull(getSupportActionBar()).setTitle(z.getNome());


        dataBaseHelper = new DataBaseHelper(this);

        nascondiView();
    }

    /**
     * Questo metodo consente di nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite
     */
    private void nascondiView() {
        dataBaseHelper = new DataBaseHelper(this);

        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        //email dell'account ospite
        String emailOspite = "admin@gmail.com";
        if(emailCuratore.compareTo(emailOspite) == 0){
            aggiungiOggettoButton.setVisibility(View.INVISIBLE);
            eliminaZonaButton.setVisibility(View.INVISIBLE);
            modificaZona.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Zona zM = dataBaseHelper.recuperoZonaModificata(z.getId());
        nomeZona.setText(zM.getNome());
        descrizioneZona.setText(zM.getDescrizione());

    }

    @Override
    protected void onStart() {
        super.onStart();

        modificaZona.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ModificaZonaActivity.class);
            intent.putExtra("ZONE", z);
            startActivity(intent);
        });

        aggiungiOggettoButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AggiungiOggettoActivity.class)));
        eliminaZonaButton.setOnClickListener(view -> showCustomDialog());
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

        conferma.setOnClickListener(v -> {
            dataBaseHelper.rimuoviZona(z);
            dialog.dismiss();
            finish();
        });
        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }


}
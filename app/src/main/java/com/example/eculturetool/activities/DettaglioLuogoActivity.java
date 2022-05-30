package com.example.eculturetool.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DettaglioLuogoActivity extends AppCompatActivity {

    private static final int MIN_LUOGHI = 1;    //numero minimo di luoghi che devono essere presenti

    private DataBaseHelper dataBaseHelper;      //oggetto che consente di interrogare il database per reperire dati
    private Luogo luogoSelezionato;             //luogo selezionato dalla recyclerView precedente
    private int idLuogo;                        //id del luogo selezioanto nella recyclerview prededente e ottenuto tramite intent
    private TextView nomeLuogo, descrizioneLuogo, tipologiaLuogo;

    private Button impostaLuogoCorrente;
    private Button eliminaLuogo;
    private FloatingActionButton editLuogo;

    private int numeroLuoghi;
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
        dataBaseHelper = new DataBaseHelper(this);

        //Metodo di scroll per la textView
        descrizioneLuogo.setMovementMethod(new ScrollingMovementMethod());

        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        idLuogo = intent.getIntExtra(Luogo.Keys.ID, 0);     //id del luogo selezionato nella recyclerView precedente

        luogoSelezionato = dataBaseHelper.getLuogoById(idLuogo);
        luoghiList = dataBaseHelper.getLuoghi();                        //elenco di tutti luoghi relativi a un curatore

        //Metodo che consente di nascondere le view in caso di accesso con l'account ospite
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
            impostaLuogoCorrente.setVisibility(View.INVISIBLE);
            eliminaLuogo.setVisibility(View.INVISIBLE);
            editLuogo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        popolaCampi();

        impostaLuogoCorrente.setOnClickListener(view -> {
            dataBaseHelper.setLuogoCorrente(idLuogo);
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        });

        editLuogo.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ModificaLuogoActivity.class);
            intent.putExtra(Luogo.Keys.ID, idLuogo);
            startActivity(intent);
        });

        eliminaLuogo();
    }

    private void popolaCampi() {
        if(luogoSelezionato != null){
            Objects.requireNonNull(getSupportActionBar()).setTitle(luogoSelezionato.getNome());
            nomeLuogo.setText(luogoSelezionato.getNome());
            descrizioneLuogo.setText(luogoSelezionato.getDescrizione());
            tipologiaLuogo.setText(setTipologia(luogoSelezionato.getTipologia()));
        }
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


    private void eliminaLuogo() {

        //Ottengo il numero di luoghi creato da un certo curatore
        numeroLuoghi = luoghiList.size();
        int luogoCorrente = dataBaseHelper.getLuogoCorrente().getId();

        eliminaLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numeroLuoghi == MIN_LUOGHI) {
                    Toast.makeText(DettaglioLuogoActivity.this, getResources().getString(R.string.min_luoghi), Toast.LENGTH_LONG).show();
                } else {
                    showCustomDialog(luogoCorrente);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        luogoSelezionato = dataBaseHelper.getLuogoById(idLuogo);
        popolaCampi();

    }

    public void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.avviso))
                .setMessage(getResources().getString(R.string.avviso_luoghi_zone))
                .setNeutralButton("Ok", (dialogInterface, i) -> finish());
        alert.create().show();
    }

    private void showCustomDialog(int luogoCorrente) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);

        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText(getResources().getString(R.string.cancella_luogo));
        TextView descrizione_tv = layout.findViewById(R.id.descrizione_dialog);
        descrizione_tv.setText(getResources().getString(R.string.NB_luogo));


        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();
        conferma.setOnClickListener(v -> {
            if (idLuogo == luogoCorrente) {

                for(int i = 0; i < numeroLuoghi; i++){
                    if(luogoCorrente != luoghiList.get(i).getId()){
                        dataBaseHelper.setLuogoCorrente(luoghiList.get(i).getId());
                        dataBaseHelper.deleteLuogo(idLuogo);
                        dialog.dismiss();
                        break;
                    }
                }
            } else {
                dataBaseHelper.deleteLuogo(idLuogo);
                dialog.dismiss();
            }
            showDialog();
        });
        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }
}
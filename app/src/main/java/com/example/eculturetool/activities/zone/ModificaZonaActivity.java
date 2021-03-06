package com.example.eculturetool.activities.zone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;
import java.util.List;

public class ModificaZonaActivity extends AppCompatActivity {

    private EditText nomeZona, descrizioneZona;
    private ImageView frecciaBack, conferma;
    private List<Zona> zoneList = new ArrayList<>();
    private ProgressBar progressBar;
    private Zona z1;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_zona);

        //prendo i riferimenti delle view del layout
        nomeZona = findViewById(R.id.nomeZonaModifica);
        descrizioneZona = findViewById(R.id.descrizioneZonaModifica);
        frecciaBack = findViewById(R.id.freccia_back_modifica_zona);
        conferma = findViewById(R.id.icona_conferma_modifica_zona);
        progressBar=findViewById(R.id.progressModificaZona);

        //recupero dati dall'intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        z1 = (Zona) bundle.getSerializable("ZONE");

        dataBaseHelper = new DataBaseHelper(this);
        zoneList = dataBaseHelper.getZone();
    }

    @Override
    protected void onStart() {
        super.onStart();

        nomeZona.setText(z1.getNome());
        descrizioneZona.setText(z1.getDescrizione());

        frecciaBack.setOnClickListener(view -> finish());

        conferma.setOnClickListener(view -> editZona());
    }


//avvaloro la zona corrispondente verificandone la correttezza delle informazioni immesse
    private void editZona() {
        String nome = nomeZona.getText().toString().trim();
        String descrizione = descrizioneZona.getText().toString().trim();


        if (nome.isEmpty()) {
            nomeZona.setError(getResources().getString(R.string.nome_zona_richiesto));
            nomeZona.requestFocus();
            return;
        }

        if (controlloEsistenzaNomeZona(nome)) {
            nomeZona.requestFocus();
            nomeZona.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneZona.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneZona.requestFocus();
            return;
        }

        Zona z2 = new Zona(0, nome, descrizione, 0);

        progressBar.setVisibility(View.INVISIBLE);

        dataBaseHelper.modifica(z1, z2);
        finish();
    }
//controllo se esiste  una zona con il nome specificato
    private boolean controlloEsistenzaNomeZona(String nomeZona) {
        boolean isEsistente = false;

        List<Zona> zoneAppoggio = new ArrayList<>();
        for(Zona zona: zoneList){
            if(!(zona.getId() == z1.getId())){
                zoneAppoggio.add(zona);
            }
        }

        for (int i = 0; i < zoneAppoggio.size(); i++) {
            if (nomeZona.compareToIgnoreCase(zoneAppoggio.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }

        return isEsistente;
    }
}
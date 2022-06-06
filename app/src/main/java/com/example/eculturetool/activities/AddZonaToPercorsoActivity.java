package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.RecyclerAdapterCheckbox;
import com.example.eculturetool.utility_percorsi.CheckboxListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Permette di aggiungere una zona in un percorso
 */
public class AddZonaToPercorsoActivity extends AppCompatActivity implements CheckboxListener {

    //Recupero istanza del dataholder, contenente la lista di zone attualmente in uso per creare il percorso
    private DataHolder data = DataHolder.getInstance();

    RecyclerView recyclerView;
    RecyclerAdapterCheckbox recyclerAdapterCheckbox;

    MaterialButton btnConferma;
    MaterialButton btnAddZona;

    DataBaseHelper dataBaseHelper;
    ArrayList<Zona> listaZone;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zona_to_percorso);

        //Settaggio del titolo all'activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.aggiungi_zona));

        recyclerView = findViewById(R.id.recyclerViewCheckbox);
        btnConferma = findViewById(R.id.btn_conferma);
        btnAddZona = findViewById(R.id.btnAggiungiZona);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        prepList();
        init();

        //Setting bottone conferma
        btnConferma.setOnClickListener(v -> {
            data.getData().addAll(recyclerAdapterCheckbox.getSelectedList());
            finish();
            Intent intent = new Intent (AddZonaToPercorsoActivity.this, CreazionePercorsoActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onQuantityChange(ArrayList<Zona> arrayList) {
    }

    /**
     * Alla pressione del tasto indietro, si ritorna nell'activity di creazione del percorso
     */
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent (AddZonaToPercorsoActivity.this, CreazionePercorsoActivity.class);
        startActivity(intent);
    }

    /**
     * Rimuove le zone già inserite dalle possibili opzioni selezionabili, per evitare che una diramazione contenga il nodo padre
     */
    private void prepList() {
        //Recupero zone del luogo corrente, da database
        listaZone = (ArrayList<Zona>) dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId());

        for(int i = 0; i < data.getData().size(); i++){
            for(int j = 0; j < listaZone.size(); j++){
                if(data.getData().get(i).getNome().equals(listaZone.get(j).getNome()))
                    listaZone.remove(j--);
            }
        }
    }

    /**
     * Inizializzazione della recyclerView e setting adapter
     */
    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterCheckbox = new RecyclerAdapterCheckbox(this, listaZone, this);
        recyclerView.setAdapter(recyclerAdapterCheckbox);
    }

}

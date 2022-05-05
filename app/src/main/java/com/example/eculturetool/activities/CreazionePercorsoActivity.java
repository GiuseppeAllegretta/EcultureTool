package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.MyItemTouchHelperCallback;
import com.example.eculturetool.utility_percorsi.MyRecyclerAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreazionePercorsoActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewZone)
    RecyclerView recyclerView;

    MaterialButton btnConferma;

    DataBaseHelper dataBaseHelper;
    ItemTouchHelper itemTouchHelper;

    //Lista di tutte le zone di un luogo
    ArrayList<Zona> listaZone = new ArrayList<>();
    //Lista di tutte le zone selezionate per la creazione del percorso
    ArrayList<Zona> listaZoneSelezionate = new ArrayList<>();
    //Lista dei nomi delle zone selezionate

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_percorso);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        btnConferma = findViewById(R.id.btnConfermaPercorso);

        btnConferma.setOnClickListener(v -> {
            //savePercorso();
        });

        //Recupero zone del luogo corrente
        listaZone.addAll(dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId()));
        listaZoneSelezionate.addAll(listaZone);

        //Recupero oggetti per ogni zona
        for(int i = 0; i < listaZone.size(); i++){
            listaZone.get(i).addListaOggetti(dataBaseHelper.getOggettiByZona(listaZone.get(i)));
        }

        init();
        generateItems();
    }


   /* private void addCard() {
        Intent intent = new Intent (this, SelectMissingZones.class);
        intent.putExtra("zoneTotali", listaZone);
        intent.putExtra("zoneSelezionate", listaZoneSelezionate);
        startActivity(intent);

    }*/


    /**
     * Metodo che permette la creazione delle cards zone
     */
    private void generateItems() {
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, listaZone, viewHolder -> itemTouchHelper.startDrag(viewHolder));

        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Metodo che inizializza la recycler view (griglia)
     */
    private void init(){
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    //TODO auto increment per l'id nel db?
    /*private void savePercorso(){
        Percorso percorso = new Percorso(1, "stub", )
    }*/


}

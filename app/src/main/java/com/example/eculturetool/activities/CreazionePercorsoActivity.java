package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.MyItemTouchHelperCallback;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterGrid;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreazionePercorsoActivity extends AppCompatActivity {


    @BindView(R.id.recyclerViewZone)
    RecyclerView recyclerView;

    MaterialButton btnConferma;
    MaterialButton btnAggiungiZona;

    DataBaseHelper dataBaseHelper;
    ItemTouchHelper itemTouchHelper;

    //ArrayList condiviso, memorizza le zone selezionate
    DataHolder data = DataHolder.getInstance();

    ArrayList<Zona> listaZone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_percorso);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        btnConferma = findViewById(R.id.btnConfermaPercorso);
        btnAggiungiZona = findViewById(R.id.btnAggiungiZona);


        //Recupero zone del luogo corrente
        listaZone = (ArrayList<Zona>) dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId());

        //Recupero oggetti per ogni zona
        for(int i = 0; i < listaZone.size(); i++){
            listaZone.get(i).addListaOggetti(dataBaseHelper.getOggettiByZona(listaZone.get(i)));
        }

        init();
        generateItems();

        btnAggiungiZona.setOnClickListener(v ->{
            finish();
            Intent intent = new Intent(this, AddZonaToPercorsoActivity.class);
            startActivity(intent);

        });


        btnConferma.setOnClickListener(v -> {
            //savePercorso();
        });

    }


    /**
     * Metodo che permette la creazione delle cards zone
     */
    private void generateItems() {
        RecyclerAdapterGrid adapter = new RecyclerAdapterGrid(this, data.getData(), viewHolder -> itemTouchHelper.startDrag(viewHolder));

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

    //TODO auto increment per l'id nel db? Effettuare scrittura nel db
    /*private void savePercorso(){
        Percorso percorso = new Percorso(1, "stub", )?
        data.getData().clear(); // pulizia array condiviso tra le classi per evitare che riaprendo l'activity resti memorizzato
    }*/


}

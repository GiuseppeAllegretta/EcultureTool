package com.example.eculturetool.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.MyRecyclerAdapter;
import com.example.eculturetool.utility_percorsi.MyItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreazionePercorsoActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewZone)
    RecyclerView recyclerView;
    ImageButton btnAdd;

    DataBaseHelper dataBaseHelper;
    ItemTouchHelper itemTouchHelper;



    //Dati simulati, TODO prenderli da database
    Luogo luogo;
    ArrayList<Zona> listaZone = new ArrayList<>();
    static List<String> data = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_percorso);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());


        luogo = dataBaseHelper.getLuogoCorrente();
        //btnAdd = findViewById(R.id.btnAddZona);

        // Metodo corretto, per testare uso la demo
        /*listaZone.addAll(dataBaseHelper.getZone());
        for(int i = 0; i < listaZone.size(); i++){
            listaZone.get(i).addListaOggetti(dataBaseHelper.getOggettiByZona(listaZone.get(i)));
        }*/

        listaZone.addAll(dataBaseHelper.getZoneDemo());


        init();
        generateItems();

        //btnAdd.setOnClickListener(view -> addCard());

    }




    private void addCard() {
        data.add("Sette");
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

    private void showButton(){
        //TODO creare un altro adapter per il bottone
    }

    /**
     * Metodo che permette la creazione delle cards
     */
    private void generateItems() {
        //Preparazione dei nomi delle card
        for (int i = 0; i < listaZone.size(); i++){
            data.add(i, listaZone.get(i).getNome());
        }
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, data, viewHolder -> itemTouchHelper.startDrag(viewHolder));

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


}

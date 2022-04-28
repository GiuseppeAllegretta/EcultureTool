package com.example.eculturetool.activities;

import android.os.Bundle;
import android.text.Layout;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.MyRecyclerAdapter;
import com.example.eculturetool.utility_percorsi.MyItemTouchHelperCallback;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreazionePercorsoActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewZone)
    RecyclerView recyclerView;
    ImageButton btnAdd;

    ItemTouchHelper itemTouchHelper;

    //Dati simulati, TODO prenderli da database
    Luogo luogo = new Luogo("Museo di Matt", "Descrizione", Tipologia.MUSEO, "emailacaso");
    static List<String> data = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_percorso);
        //btnAdd = findViewById(R.id.btnAddZona);


        //Simulazione dati
        luogo.addZona(new Zona(1, "Stanza delle cere", "Nella stanza si possono trovare cere di vario tipo", 1));
        luogo.addZona(new Zona(2, "Stanza delle statue ma con un nome lungo", "Molte statue, poco da dire", 1));
        luogo.addZona(new Zona(4, "Camera rossa", "E' una camera rossa", 1));
        luogo.addZona(new Zona(6, "Retro", "Finalmente ce ne andiamo", 1));
        luogo.addZona(new Zona(9, "Salone principale", "Grande quasi quanto il mio sgabuzzino", 1));
        data.addAll(luogo.getZoneAsStringList());

        init();
        generateItem();

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

    private void generateItem() {
        MyRecyclerAdapter adapter = new MyRecyclerAdapter(this, data, viewHolder -> itemTouchHelper.startDrag(viewHolder));

        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void init(){
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }


}

package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.utilities.RecyclerAdapterPercorso;

import java.util.ArrayList;

public class PercorsiActivity extends AppCompatActivity {

    private ArrayList<Percorso> percorsiList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorsi);

        //Inizializzazione variabili
        recyclerView = findViewById(R.id.recyclerViewPercorsi);
        percorsiList = new ArrayList<>();

        setPercorsiInfo();
        setAdapter();
    }

    private void setAdapter() {
        RecyclerAdapterPercorso adapterPercorso = new RecyclerAdapterPercorso(percorsiList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPercorso);
    }

    private void setPercorsiInfo() {
        percorsiList.add(new Percorso("1", "Colosseo: base"));
        percorsiList.add(new Percorso("2", "Colosseo: intermedio"));
        percorsiList.add(new Percorso("3", "Colosseo: avanzato"));
    }
}
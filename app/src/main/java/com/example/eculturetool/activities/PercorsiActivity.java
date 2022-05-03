package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.utilities.RecyclerAdapterPercorso;

import java.util.ArrayList;

public class PercorsiActivity extends AppCompatActivity {

    private ArrayList<Percorso> percorsiList;
    private RecyclerView recyclerView;
    private RecyclerAdapterPercorso adapterPercorso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorsi);

        //Inizializzazione variabili
        recyclerView = findViewById(R.id.recyclerViewPercorsi);
        percorsiList = new ArrayList<>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarPercorsi);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ritorna al fragment del profilo chiamante
                finish();
            }
        });

        setPercorsiInfo();
        setAdapter();
    }

    private void setAdapter() {
        adapterPercorso = new RecyclerAdapterPercorso(percorsiList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPercorso);
    }

    private void setPercorsiInfo() {
        //percorsiList.add(new Percorso("1", "Colosseo: base"));
        //percorsiList.add(new Percorso("2", "Colosseo: intermedio"));
        //percorsiList.add(new Percorso("3", "Colosseo: avanzato"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.oggetti_menu, menu);
        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterPercorso.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
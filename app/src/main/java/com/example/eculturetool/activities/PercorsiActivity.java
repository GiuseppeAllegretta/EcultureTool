package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.utilities.RecyclerAdapterPercorso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PercorsiActivity extends AppCompatActivity {

    private ArrayList<Percorso> percorsiList;
    private RecyclerView recyclerView;
    private RecyclerAdapterPercorso adapterPercorso;
    private FloatingActionButton addPercorsoFbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorsi);

        //Inizializzazione variabili
        recyclerView = findViewById(R.id.recyclerViewPercorsi);
        addPercorsoFbt = findViewById(R.id.addPercorso);
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

    @Override
    protected void onStart() {
        super.onStart();

        addPercorsoFbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PercorsiActivity.this, DatiPercorsoActivity.class));
            }
        });
    }

    private void setAdapter() {
        adapterPercorso = new RecyclerAdapterPercorso(percorsiList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPercorso);
    }

    private void setPercorsiInfo() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        percorsiList.addAll(dataBaseHelper.getPercorsi());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
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
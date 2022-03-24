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

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterZona;
import com.example.eculturetool.entities.Zona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ZoneActivity extends AppCompatActivity implements RecyclerAdapterZona.OnZonaListener{

    private ArrayList<Zona> zoneList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddLuogo;
    RecyclerAdapterZona adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        fabAddLuogo = findViewById(R.id.addZona);


        Toolbar myToolbar = findViewById(R.id.toolbarZone);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(view -> {
            //Ritorna al fragment del profilo chiamante
            finish();
        });


        recyclerView=findViewById(R.id.recyclerViewZone);
        zoneList=new ArrayList<>();

        setZoneInfo();
        setAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fabAddLuogo.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AggiungiZonaActivity.class)));

    }

    private void setZoneInfo(){
    }

    private void setAdapter(){
        adapter= new RecyclerAdapterZona(zoneList,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onZonaClick(int position) {
        zoneList.get(position);
        System.out.println("POSIZIONE CLICCATA --> "+position);
        Intent intent= new Intent(this,DettaglioZonaActivity.class);
       // intent.putExtra("oggetto cliccato",valore);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.oggetti_menu,menu);
        MenuItem item=menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterZona;
import com.example.eculturetool.entities.Zona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ZoneActivity extends AppCompatActivity implements RecyclerAdapterZona.OnZonaListener{

    private ArrayList<Zona> zoneList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddLuogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        fabAddLuogo = findViewById(R.id.addZona);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarZone);

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


        recyclerView=findViewById(R.id.recyclerViewZone);
        zoneList=new ArrayList<>();

        setZoneInfo();
        setAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fabAddLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiZonaActivity.class));
            }
        });

    }

    private void setZoneInfo(){
        zoneList.add(new Zona("Zona 1","La zona 1",10));
        zoneList.add(new Zona("Zona 2","La zona 2",10));
        zoneList.add(new Zona("Zona 3","La zona 3",10));
        zoneList.add(new Zona("Zona 4","La zona 4",10));
        zoneList.add(new Zona("Zona 5","La zona 5",10));
    }

    private void setAdapter(){
        RecyclerAdapterZona adapter= new RecyclerAdapterZona(zoneList,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onZonaClick(int position) {
        zoneList.get(position);
        System.out.println("POSIZIONE CLICCATA -->"+position);
        Intent intent= new Intent(this,DettaglioZonaActivity.class);
       // intent.putExtra("oggetto cliccato",valore);
        startActivity(intent);
    }
}
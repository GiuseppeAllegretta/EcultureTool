package com.example.eculturetool.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterOggetto;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Oggetto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OggettiActivity extends AppCompatActivity {
    //private final Connection connection = new Connection();

    private ArrayList<Oggetto> oggettiList;
    private RecyclerView recyclerView;


    private FloatingActionButton fabAddOggetto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oggetti);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarOggetti);

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

        recyclerView = findViewById(R.id.recyclerViewOggetti);
        oggettiList = new ArrayList<>();

        setOggettoInfo();
        setAdapter();

    }

    private void setAdapter(){
        System.out.println("OGGETTI --> "+oggettiList);
        RecyclerAdapterOggetto adapter= new RecyclerAdapterOggetto(oggettiList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOggettoInfo() {
        //popolo array di oggetti
        oggettiList.add(new Oggetto("id1", "nome1","descrizione","urlImmagine"));
        oggettiList.add(new Oggetto("id2", "nome2","descrizione","urlImmagine"));
        oggettiList.add(new Oggetto("id3", "nome3","descrizione","urlImmagine"));
        oggettiList.add(new Oggetto("id4", "nome4","descrizione","urlImmagine"));
        oggettiList.add(new Oggetto("id5", "nome5","descrizione","urlImmagine"));
        oggettiList.add(new Oggetto("id6", "nome6","descrizione","urlImmagine"));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.oggetti_menu, menu);
        return true;
    }


}
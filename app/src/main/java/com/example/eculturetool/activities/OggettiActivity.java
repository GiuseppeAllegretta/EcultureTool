package com.example.eculturetool.activities;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterOggetto;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OggettiActivity extends AppCompatActivity implements RecyclerAdapterOggetto.OnOggettoListener {
    private final Connection connection = new Connection();

    private ArrayList<Oggetto> oggettiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapterOggetto adapter;

    //luogo corrente che sta gestendo il curatore
    private String luogoCorrente;
    private List<Zona> zoneList = new ArrayList<>();
    private ValueEventListener valueEventListenerZone;
    private FloatingActionButton fabAddOggetto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oggetti);
        fabAddOggetto = findViewById(R.id.addOggetto);
        recyclerView = findViewById(R.id.recyclerViewOggetti);

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

        retrieveZone();
        setOggettoInfo();
        setAdapter();
    }

    /**
     * Metodo che recupera le zone
     */
    private void retrieveZone() {
        valueEventListenerZone = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                Iterable<DataSnapshot> iteratoreZone = snapshot.getChildren();

                for (int i = 0; i < count; i++) {
                    zoneList.add(iteratoreZone.iterator().next().getValue(Zona.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        connection.getRefCuratore().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Curatore.class) != null) {
                    luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();
                    connection.getRefZone().child(luogoCorrente).addValueEventListener(valueEventListenerZone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        fabAddOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiOggettoActivity.class));
            }
        });

    }

    private void setAdapter() {
        adapter = new RecyclerAdapterOggetto(oggettiList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOggettoInfo() {
        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    luogoCorrente = snapshot.getValue(String.class).toString();

                    //popolo array di oggetti
                    connection.getRefOggetti().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            oggettiList.clear();
                            for (Zona zona : zoneList) {
                                Iterable<DataSnapshot> iteratore = snapshot.child(zona.getId()).getChildren();
                                int count = (int) snapshot.child(zona.getId()).getChildrenCount();

                                if (count != 0) {
                                    for (int i = 0; i < count; i++) {
                                        oggettiList.add(iteratore.iterator().next().getValue(Oggetto.class));
                                    }
                                }
                            }
                            setAdapter();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


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
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


    @Override
    public void onOggettoClick(int position) {
        String oggettoSelezionato = oggettiList.get(position).getId();
        Intent intent = new Intent(this, DettaglioOggettoActivity.class);
        intent.putExtra(Oggetto.Keys.ID, oggettoSelezionato);
        intent.putExtra(Luogo.Keys.ID, luogoCorrente);
        intent.putExtra("ZONELIST", (Serializable) zoneList);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.getRefZone().child(luogoCorrente).removeEventListener(valueEventListenerZone);
    }
}
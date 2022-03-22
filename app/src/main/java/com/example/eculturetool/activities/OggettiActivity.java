package com.example.eculturetool.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterOggetto;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OggettiActivity extends AppCompatActivity {
    private final Connection connection = new Connection();

    private ArrayList<Oggetto> oggettiList;
    private RecyclerView recyclerView;

    //luogo corrente che sta gestendo il curatore
    private String luogoCorrente;


    private FloatingActionButton fabAddOggetto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oggetti);
        fabAddOggetto = findViewById(R.id.addOggetto);
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

//        connection.getRefCuratore().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue(Curatore.class) != null) {
//
//                    //Ottengo il luogo corrente del curatore
//                    luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();
//
//                    connection.getRefOggetti().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Iterable<DataSnapshot> iteratore = snapshot.getChildren();
//                            int count = (int) snapshot.getChildrenCount();
//                            System.out.println("count: " + count);
//
//                            oggettiList.clear();
//                            for (int i = 0; i < count; i++) {
//                                oggettiList.add(iteratore.iterator().next().getValue(Oggetto.class));
//                                //Luogo luogoprova= new Luogo("scavo","ciao", Tipologia.SITO_CULTURALE,Connection.getUidCuratore());
//                                //luoghiList.add(luogoprova);
//                                System.out.println(oggettiList.get(i));
//                            }
//                            setAdapter();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.oggetti_menu, menu);
        return true;
    }


}
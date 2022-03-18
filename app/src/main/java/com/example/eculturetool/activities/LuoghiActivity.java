package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.AdapterView;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapter;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LuoghiActivity extends AppCompatActivity{

    private final Connection connection = new Connection();

    private ArrayList<Luogo> luoghiList;
    private RecyclerView recyclerView;

    private String luogoCorrente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luoghi);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarLuoghi);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recyclerView);
        luoghiList = new ArrayList<>();

        setLuogoInfo();

    }

    private void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(luoghiList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setLuogoInfo() {
        connection.getRefCuratore().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Curatore.class) != null){

                    //Ottengo il luogo corrente del curatore
                    luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();

                    connection.getRefLuogo().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                            int count = (int) snapshot.getChildrenCount();
                            System.out.println("count: " +count);

                            for(int i = 0; i < count; i++){
                                luoghiList.add(iteratore.iterator().next().getValue(Luogo.class));
                                System.out.println(luoghiList.get(i));
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



}
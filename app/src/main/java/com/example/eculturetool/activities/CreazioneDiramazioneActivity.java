package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterList;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CreazioneDiramazioneActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    DataHolder data = DataHolder.getInstance();
    ArrayList<Zona> childs = new ArrayList<>(); //Array che esclude il nodo padre
    Intent intent;

    RecyclerView recyclerView;
    RecyclerAdapterList<Zona> recyclerAdapterList;
    ItemTouchHelper itemTouchHelper;

    MaterialButton btnConferma;
    MaterialButton btnReset;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_diramazione);

        recyclerView = findViewById(R.id.recyclerViewList);
        btnConferma = findViewById(R.id.btn_conferma);
        btnReset = findViewById(R.id.btn_reset);

        //Inserimento del titolo all'activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.Creazione_diramazione));

        //Preparazione items, rimuovendo il nodo padre
        intent = getIntent();
        String root = intent.getStringExtra("ROOT");
        int number = Integer.parseInt(intent.getStringExtra("NUMBER"));

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        childs.addAll(dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId()));
        prepData(root);

        //Inizializzazione recycler view
        initRecyclerView();

        btnConferma.setOnClickListener(v -> {
            //Acquisisco la posizione della card per impostare la diramazione
            data.getData().get(number - 1).setDiramazione(childs);
            finish();
            Intent intent = new Intent (CreazioneDiramazioneActivity.this, CreazionePercorsoActivity.class);
            startActivity(intent);
        });

        btnReset.setOnClickListener(v -> {
            finish();
            overridePendingTransition( 0, 0);
            Intent intent = new Intent (CreazioneDiramazioneActivity.this, CreazioneDiramazioneActivity.class);
            intent.putExtra("ROOT", root);
            intent.putExtra("NUMBER", ""+number);
            startActivity(intent);
            overridePendingTransition( 0, 0);
        });

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(childs, fromPosition, toPosition);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            childs.remove(position);
            recyclerAdapterList.notifyDataSetChanged();
        }
    };

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent (CreazioneDiramazioneActivity.this, CreazionePercorsoActivity.class);
        startActivity(intent);
    }


    private void prepData(String root){
        childs.removeIf(item -> item.getNome().equals(root));
    }

    private void initRecyclerView(){
        recyclerAdapterList = new RecyclerAdapterList<>(childs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapterList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

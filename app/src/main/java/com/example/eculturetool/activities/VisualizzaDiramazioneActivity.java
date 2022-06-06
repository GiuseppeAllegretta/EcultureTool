package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterList;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

/**
 * Permette di visualizzare le possibili diramazioni a partire da una zona, in un percorso
 */
public class VisualizzaDiramazioneActivity extends AppCompatActivity {

    private DataHolder data = DataHolder.getInstance();
    private Intent intent;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_diramazione);

        TextView titolo = findViewById(R.id.titolo);
        recyclerView = findViewById(R.id.recyclerViewList);
        MaterialButton btnReimposta = findViewById(R.id.btn_reimposta);

        //Inserimento del titolo all'activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.Visualizza_diramazione));

        intent = getIntent();
        String root = intent.getStringExtra("ROOT");
        int number = Integer.parseInt(intent.getStringExtra("NUMBER"));
        titolo.setText(root);

        //Inizializzazione recycler view
        initRecyclerView();

        //Setting bottone "reimposta", permette di modificare le diramazioni
        btnReimposta.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent (VisualizzaDiramazioneActivity.this, CreazioneDiramazioneActivity.class);
            intent.putExtra("ROOT", root);
            intent.putExtra("NUMBER", ""+number);
            startActivity(intent);
        });

        //Imposta la freccia back visibile nella actionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            Intent intent = new Intent (VisualizzaDiramazioneActivity.this, CreazionePercorsoActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Alla pressione del tasto indietro l'activity termina
     */
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent (VisualizzaDiramazioneActivity.this, CreazionePercorsoActivity.class);
        startActivity(intent);
    }

    /**
     * Inizializzazione della recyclerView e setting adapter
     */
    private void initRecyclerView(){
        RecyclerAdapterList<Zona> recyclerAdapterList = new RecyclerAdapterList<>(data.getData().get(Integer.parseInt(intent.getStringExtra("NUMBER")) - 1).getDiramazione());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapterList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}

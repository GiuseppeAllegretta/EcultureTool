package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.RecyclerAdapterCheckbox;
import com.example.eculturetool.utility_percorsi.CheckboxListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AddZonaToPercorsoActivity extends AppCompatActivity implements CheckboxListener {

    private DataHolder data = DataHolder.getInstance();

    RecyclerView recyclerView;
    RecyclerAdapterCheckbox recyclerAdapterCheckbox;

    MaterialButton btnConferma;
    MaterialButton btnAddZona;

    DataBaseHelper dataBaseHelper;
    ArrayList<Zona> listaZone;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zona_to_percorso);

        recyclerView = findViewById(R.id.recyclerViewCheckbox);
        btnConferma = findViewById(R.id.btn_conferma);
        btnAddZona = findViewById(R.id.btnAggiungiZona);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        //Recupero zone del luogo corrente
        listaZone = (ArrayList<Zona>) dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId());

        init();

        btnConferma.setOnClickListener(v -> {
            data.getData().addAll(recyclerAdapterCheckbox.getSelectedList());
            finish();
            Intent intent = new Intent (AddZonaToPercorsoActivity.this, CreazionePercorsoActivity.class);
            startActivity(intent);
        });

    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterCheckbox = new RecyclerAdapterCheckbox(this, listaZone, this);
        recyclerView.setAdapter(recyclerAdapterCheckbox);
    }


    @Override
    public void onQuantityChange(ArrayList<Zona> arrayList) {
    }

}

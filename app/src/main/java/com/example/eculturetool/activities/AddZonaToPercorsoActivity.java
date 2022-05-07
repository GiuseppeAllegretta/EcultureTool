package com.example.eculturetool.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterList;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;

public class AddZonaToPercorsoActivity extends AppCompatActivity {

    //Classe singleton che mi permette di passare i dati tra le activity
    ArrayList<Zona> data = DataHolder.getInstance().getData();

    RecyclerView recyclerView;
    RecyclerAdapterList recyclerAdapterList;

    MaterialButton btnSave;
    MaterialButton btnAddZona;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zona_to_percorso);


        recyclerView = findViewById(R.id.recyclerViewList);
        recyclerAdapterList = new RecyclerAdapterList(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapterList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        btnSave = findViewById(R.id.btnSalvaDiramazione);
        btnAddZona = findViewById(R.id.btnAggiungiZona);

        btnAddZona.setOnClickListener(v ->{

        });

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(data, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}

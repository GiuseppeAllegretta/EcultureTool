package com.example.eculturetool.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterList;

public class InfoZonaActivity extends AppCompatActivity {

    ImageView close;
    TextView titolo;
    TextView descrizione;

    RecyclerView recyclerView;
    RecyclerAdapterList recyclerAdapterList;

    public InfoZonaActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_zona);
        titolo = findViewById(R.id.titolo);
        titolo.setText(getIntent().getStringExtra("TITLE"));

        descrizione = findViewById(R.id.descrizione);
        descrizione.setText(getIntent().getStringExtra("DESCRIPTION"));

        getSupportActionBar().setTitle(getString(R.string.descrizione_zona));

        int posizione = getIntent().getIntExtra("POSITION", -1);

        recyclerView = findViewById(R.id.list_oggetti);
        recyclerAdapterList = new RecyclerAdapterList(DataHolder.getInstance().getData().get(posizione).getListaOggetti());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapterList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        close = findViewById(R.id.ic_close);
        close.setOnClickListener(v -> finish());
        System.out.println(posizione + "  " + DataHolder.getInstance().getData().get(posizione).getListaOggetti());
    }
    
}

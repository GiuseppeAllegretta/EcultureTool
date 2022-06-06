package com.example.eculturetool.activities.percorsi;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.RecyclerAdapterPercorso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Permette di visualizzare l'elenco dei percorsi creati, ricercare un percorso attraverso il suo nome ed accedere alle sue informazioni
 */
public class PercorsiActivity extends AppCompatActivity implements RecyclerAdapterPercorso.OnPercorsoListener {

    private ArrayList<Percorso> percorsiList;
    private RecyclerView recyclerView;
    private RecyclerAdapterPercorso adapterPercorso;
    private FloatingActionButton addPercorsoFbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percorsi);

        recyclerView = findViewById(R.id.recyclerViewPercorsi);
        addPercorsoFbt = findViewById(R.id.addPercorso);
        percorsiList = new ArrayList<>();

        Toolbar myToolbar = findViewById(R.id.toolbarPercorsi);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(view -> {
            //Ritorna al fragment del profilo chiamante
            finish();
        });

        setPercorsiInfo();
        setAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Setting del floating button per l'aggiunta di un nuovo percorso
        addPercorsoFbt.setOnClickListener(view -> {
            DataHolder data = DataHolder.getInstance();
            data.getData().clear();
            data.setPathName("");
            startActivity(new Intent(PercorsiActivity.this, CreazionePercorsoActivity.class));
        });
    }

    /**
     * Setting adapter contente un percorso
     */
    private void setAdapter() {
        adapterPercorso = new RecyclerAdapterPercorso(percorsiList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPercorso);
    }

    /**
     * Acquisizione da database dei percorsi creati
     */
    private void setPercorsiInfo() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        percorsiList.addAll(dataBaseHelper.getPercorsi());
    }

    //Menù di ricerca; attraverso il nome è possibile ritrovare un percorso specifico
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterPercorso.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Clickando su un percorso di accede alle sue informazioni
    @Override
    public void onPercorsoClick(int position) {
        IoHelper ioHelper = new IoHelper(this);

        int idPercorso = percorsiList.get(position).getId();
        Graph<Zona, DefaultEdge> graph = ioHelper.deserializzaPercorso(idPercorso);
        Intent intent = new Intent(this, RiepilogoPercorsoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ID_PERCORSO", idPercorso);
        bundle.putSerializable("grafo", (Serializable) graph);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Pulizia della lista di percorsi
    @Override
    protected void onResume() {
        super.onResume();
        percorsiList.clear();
        setPercorsiInfo();
        setAdapter();
    }
}
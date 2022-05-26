package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.view.GraphView;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class RiepilogoPercorsoActivity extends AppCompatActivity {
    private GraphView graphView;
    private Graph<Zona, DefaultEdge> graph;
    private IoHelper ioHelper;
    private int idPercorso; //id del percorso
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo_percorso);
        ioHelper = new IoHelper(this);
        dataBaseHelper = new DataBaseHelper(this);

        Intent i;
        i = getIntent();
        graph = (Graph<Zona, DefaultEdge>) i.getExtras().getSerializable("grafo");
        idPercorso = i.getIntExtra("ID_PERCORSO", 0);

        graphView = findViewById(R.id.graphView);
        graphView.setGrafo(graph);


        Toolbar myToolbar = findViewById(R.id.toolbarDettaglioPercorso);
        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        myToolbar.setTitle(dataBaseHelper.getPercorsoById(idPercorso).getNome());
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ritorna al fragment del profilo chiamante
                finish();
            }
        });

        scritturaSuFileJson();
    }

    private void scritturaSuFileJson() {
        //Appena ottenuti i dati del grafo vengno salvati tra i file
        ioHelper.esportaTxtinJsonFormat(graph, idPercorso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.percorso_menu, menu);
        MenuItem item = menu.findItem(R.id.shareTXT);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ioHelper.shareFileTxt(idPercorso);
                return false;
            }
        });

        return true;
    }
}
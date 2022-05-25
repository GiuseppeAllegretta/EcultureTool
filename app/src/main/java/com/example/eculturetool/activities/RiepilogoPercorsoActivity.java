package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.view.GraphView;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class RiepilogoPercorsoActivity extends AppCompatActivity {
    private GraphView graphView;
    private SimpleGraph<Zona, DefaultEdge> graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo_percorso);


        Intent i = new Intent();
        i = getIntent();
        graph = (SimpleGraph<Zona, DefaultEdge>) i.getExtras().getSerializable("grafo");



        graphView = findViewById(R.id.graphView);
        graphView.setGrafo(graph);
        Toolbar myToolbar = findViewById(R.id.toolbarDettaglioPercorso);



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

    }


}
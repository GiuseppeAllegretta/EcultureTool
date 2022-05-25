package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.view.GraphView;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class RiepilogoPercorsoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i = getIntent();
        SimpleGraph<Zona, DefaultEdge> graph = (SimpleGraph<Zona, DefaultEdge>) i.getExtras().getSerializable("grafo");


        //GraphView graphView = findViewById(R.id.graphView);

        //graphView.setGrafo(graph);

        System.out.println("percorso"+ graph);

        setContentView(new GraphView(this, null, graph));




    }
}
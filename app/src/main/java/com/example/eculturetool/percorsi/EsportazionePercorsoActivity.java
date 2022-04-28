package com.example.eculturetool.percorsi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.List;

public class EsportazionePercorsoActivity extends AppCompatActivity {

    //Bottone per l'esportazione di un grafo
    private Button esportaBtn;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esportazione_percorso);

        esportaBtn = findViewById(R.id.esporta);
        dataBaseHelper = new DataBaseHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        esportaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphToJson graphToJson = new GraphToJson(EsportazionePercorsoActivity.this);
                graphToJson.exportedGraphToJson(grafoProva());
            }
        });
    }


    private Graph<Zona, DefaultEdge> grafoProva() {
        Graph<Zona, DefaultEdge> graph = new SimpleGraph<Zona, DefaultEdge>(DefaultEdge.class);
        List<Zona> zoneList = dataBaseHelper.getZoneDemo();

        //Aggiunta dei vertici al grafo
        for (int i = 0; i < zoneList.size(); i++) {
            graph.addVertex(zoneList.get(i));
        }

        for (int i = 0; i < zoneList.size(); i++) {

            graph.addEdge(zoneList.get(i), zoneList.get(i + 1));
        }
        return graph;
    }
}
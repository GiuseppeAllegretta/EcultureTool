package com.example.eculturetool.percorsi;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EsportazionePercorsoActivity extends AppCompatActivity {

    //Bottone per l'esportazione di un grafo
    private Button esportaBtn;
    private Button cancellaBtn;
    private Button esportaInGrafoBtn;
    private DataBaseHelper dataBaseHelper;
    private CardView shareCrd;
    private IoHelper ioHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esportazione_percorso);

        esportaBtn = findViewById(R.id.esporta);
        cancellaBtn = findViewById(R.id.cancellaPercorso);
        esportaInGrafoBtn = findViewById(R.id.arrayAgrafo);
        shareCrd = findViewById(R.id.share);
        dataBaseHelper = new DataBaseHelper(this);
        ioHelper = new IoHelper(this);

        permessiCondivisioneFile();
    }

    private void permessiCondivisioneFile() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();

        esportaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IoHelper graphToJson = new IoHelper(EsportazionePercorsoActivity.this);
                graphToJson.serializzaPercorso(grafoProva(), 1);
                Graph<Zona, DefaultEdge> graphReturned = graphToJson.deserializzaPercorso(1);
                graphToJson.esportaTxt(grafoProva(), 1);

            }
        });

        cancellaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ioHelper.cancellaPercorso(1);
            }
        });

        shareCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ioHelper.shareFileTxt(1);
            }
        });

        esportaInGrafoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Graph<Zona, DefaultEdge> graph = ioHelper.fromListToGraph(arrayProva());
                System.out.println(graph);
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

        for (int i = 0; i < zoneList.size() - 1; i++) {
            graph.addEdge(zoneList.get(i), zoneList.get(i + 1));
        }

        for (Zona zona : zoneList) {
            zona.addOggetto(new Oggetto("Gioconda", "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio su tavola di legno di pioppo realizzato da Leonardo da Vinci (77×53 cm e 13 mm di spessore), databile al 1503-1504 circa e conservato nel Museo del Louvre di Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F15296.jpg?alt=media&token=c8403eeb-838d-4497-b891-5af4015eaefa", TipologiaOggetto.QUADRO, zona.getId()));
            zona.addOggetto(new Oggetto("Venere di Milo", "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio su tavola di legno di pioppo realizzato da Leonardo da Vinci (77×53 cm e 13 mm di spessore), databile al 1503-1504 circa e conservato nel Museo del Louvre di Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F15296.jpg?alt=media&token=c8403eeb-838d-4497-b891-5af4015eaefa", TipologiaOggetto.QUADRO, zona.getId()));
        }


        return graph;
    }



    private List<Zona> arrayProva() {
        List<Zona> zoneList = dataBaseHelper.getZoneDemo();
        List<Zona> zoneDiramazione = dataBaseHelper.getZoneDemo();

        //Elimino Descrizione
        for(int i = 0; i < zoneList.size(); i++){
            zoneList.get(i).setDescrizione("");
        }

        //Elimino Descrizione
        for(int i = 0; i < zoneDiramazione.size(); i++){
            zoneDiramazione.get(i).setDescrizione("");
        }

        for(Zona zona: zoneList){
            int p = (int)( Math.random()* zoneDiramazione.size());

            for(int i = 0; i < p; i++){
                if(zona.getId() != zoneDiramazione.get(i).getId()){
                    zona.getDiramazione().add(zoneDiramazione.get(i));
                }
            }
        }

        System.out.println("Le zone");
        for (Zona zona: zoneList){
            System.out.println(zona.toString() + "\n");
            for(Zona zona1: zona.getDiramazione()){
                System.out.println("     " + zona1.toString() + "\n");
            }
        }
        return zoneList;
    }
}
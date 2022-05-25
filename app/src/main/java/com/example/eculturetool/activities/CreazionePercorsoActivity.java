package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.MyItemTouchHelperCallback;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterGrid;
import com.google.android.material.button.MaterialButton;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreazionePercorsoActivity extends AppCompatActivity {


    @BindView(R.id.recyclerViewZone)
    RecyclerView recyclerView;

    EditText editText;
    MaterialButton btnConferma;
    MaterialButton btnAggiungiZona;

    DataBaseHelper dataBaseHelper;
    ItemTouchHelper itemTouchHelper;

    IoHelper ioHelper;

    //ArrayList condiviso, memorizza le zone selezionate
    DataHolder data = DataHolder.getInstance();

    ArrayList<Zona> listaZone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_percorso);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        editText = findViewById(R.id.nomePercorso);
        btnConferma = findViewById(R.id.btnConfermaPercorso);
        btnAggiungiZona = findViewById(R.id.btnAggiungiZona);

        ioHelper = new IoHelper(getApplicationContext());


        //Recupero zone del luogo corrente
        listaZone = (ArrayList<Zona>) dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId());

        //Recupero oggetti per ogni zona
        for(int i = 0; i < listaZone.size(); i++){
            listaZone.get(i).addListaOggetti(dataBaseHelper.getOggettiByZona(listaZone.get(i)));
        }

        init();
        generateItems();

        btnAggiungiZona.setOnClickListener(v ->{
            finish();
            Intent intent = new Intent(this, AddZonaToPercorsoActivity.class);
            startActivity(intent);

        });


        btnConferma.setOnClickListener(v -> {
            //savePercorso();

            if(data.getData().size() == 0){
                Toast.makeText(this, "Percorso vuoto", Toast.LENGTH_SHORT).show();
            } else{
                getDatiPercorso();
            }
            Bundle bundle = new Bundle();
            IoHelper ioHelper = new IoHelper(this);
            Graph<Zona, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
            graph = ioHelper.fromListToGraph(data.getData());

            bundle.putSerializable("grafo", (Serializable) graph);

            Intent intent = new Intent(this, RiepilogoPercorsoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });


    }



    private void getDatiPercorso() {
        if(editText.getText().toString().isEmpty()){
            editText.setError(getString(R.string.inserisci_nome_percorso));
            editText.requestFocus();
            return;
        }

        if(esistenzaNomePercorso()){
            editText.setError(getString(R.string.nome_esistente));
            editText.requestFocus();
            return;
        }

        Percorso percorso = new Percorso(editText.getText().toString(), dataBaseHelper.getIdLuogoCorrente());
        int idPercorso = dataBaseHelper.addPercorso(percorso);

        if(idPercorso != -1){
            percorso.setId(idPercorso);

            //TODO controllo file vuoto
            ioHelper.listZoneSerializzazione(data.getData(), idPercorso);

            Intent intent = new Intent(this, RiepilogoPercorsoActivity.class);
            intent.putExtra("PERCORSO", percorso);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Si è verificato un errore! \n Riprova", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean esistenzaNomePercorso() {
        boolean risultato = false;

        List<Percorso> percorsi = dataBaseHelper.getPercorsi();

        for(Percorso percorso: percorsi){
            if(percorso.getNome().compareToIgnoreCase(editText.getText().toString()) == 0){
                risultato = true;
            }
        }

        return risultato;
    }

    /**
     * Metodo che permette la creazione delle cards zone
     */
    private void generateItems() {
        RecyclerAdapterGrid adapter = new RecyclerAdapterGrid(this, data.getData(), viewHolder -> itemTouchHelper.startDrag(viewHolder));

        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Metodo che inizializza la recycler view (griglia)
     */
    private void init(){
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    //TODO auto increment per l'id nel db? Effettuare scrittura nel db
    private void savePercorso(){
        //Percorso percorso = new Percorso(1, "stub", )?
        //data.getData().clear(); // pulizia array condiviso tra le classi per evitare che riaprendo l'activity resti memorizzato
    }


}

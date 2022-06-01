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
    Intent intent;

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
        getSupportActionBar().setTitle(getResources().getString(R.string.creazione_percorso));

        intent = getIntent();

        //Recupero zone del luogo corrente
        listaZone = (ArrayList<Zona>) dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId());

        //Recupero oggetti per ogni zona
        for (int i = 0; i < listaZone.size(); i++) {
            listaZone.get(i).addListaOggetti(dataBaseHelper.getOggettiByZona(listaZone.get(i)));
        }

        init();
        generateItems();

        btnAggiungiZona.setOnClickListener(v -> {
            if (!editText.getText().toString().equals(""))
                data.setPathName(editText.getText().toString());

            finish();
            Intent intent = new Intent(this, AddZonaToPercorsoActivity.class);
            startActivity(intent);
        });


        btnConferma.setOnClickListener(v -> {
            boolean isValid = true;

            if(data.getData().size() == 0){
                Toast.makeText(this, "Percorso vuoto", Toast.LENGTH_SHORT).show();
                return;
            }

            if (editText.getText().toString().isEmpty()) {
                editText.setError(getString(R.string.inserisci_nome_percorso));
                editText.requestFocus();
                return;
            }

            if(data.getIdPath() != 0) {
                if (esistenzaNomePercorsoModifica()) {
                    System.out.println("BBBBBBA");
                    editText.setError(getString(R.string.nome_esistente));
                    editText.requestFocus();
                    return;
                }

                dataBaseHelper.modificaPercorso(data.getIdPath(), editText.getText().toString());
                ioHelper.listZoneSerializzazione(data.getData(), data.getIdPath());

            } else {
                if (!checkNomePercorso())
                    isValid = false;

                if(!isValid)
                    return;

                if (isValid)
                    createPercorso();
            }

            Bundle bundle = new Bundle();
            IoHelper ioHelper = new IoHelper(this);
            Graph<Zona, DefaultEdge> graph;
            graph = ioHelper.fromListToGraph(data.getData());

            //Serializzo il percorso
            ioHelper.serializzaPercorso(graph, data.getIdPath());

            bundle.putSerializable("grafo", (Serializable) graph);

            Intent intent = new Intent(this, RiepilogoPercorsoActivity.class);
            intent.putExtras(bundle);
            intent.putExtra("ID_PERCORSO", data.getIdPath());
            startActivity(intent);
            finish();
            //TODO ripulire edit text alla creazione nuovo percorso dopo averne creato uno precedentemente
            data.setIdPath(0);
            data.getData().clear();
            editText.getText().clear();
        });

    }


    /**
     * Effettua il controllo sulla validità del nome del percorso
     *
     * @return true(se il nome non è già esisistente e non è vuoto), false in ogni altro caso
     */
    private boolean checkNomePercorso() {
        boolean flag = true;

        if (editText.getText().toString().isEmpty()) {
            editText.setError(getString(R.string.inserisci_nome_percorso));
            editText.requestFocus();
            flag = false;
        }

        if (esistenzaNomePercorso()) {
            editText.setError(getString(R.string.nome_esistente));
            editText.requestFocus();
            flag = false;
        }

        return flag;
    }


    private boolean esistenzaNomePercorso() {
        boolean risultato = false;
        List<Percorso> percorsi = dataBaseHelper.getPercorsi();

        for (Percorso percorso : percorsi) {
            if (percorso.getNome().compareToIgnoreCase(editText.getText().toString()) == 0) {
                risultato = true;
            }
        }
        return risultato;
    }

    private boolean esistenzaNomePercorsoModifica() {
        boolean risultato = false;
        List<Percorso> percorsi = dataBaseHelper.getPercorsi();
        String nomePercorsoModifica = dataBaseHelper.getPercorsoById(data.getIdPath()).getNome();

        for (int i = 0; i < percorsi.size(); i++) {
            if (percorsi.get(i).getNome().compareToIgnoreCase(nomePercorsoModifica) == 0)
                percorsi.remove(i);
        }

        for (Percorso percorso : percorsi) {
            if (percorso.getNome().compareToIgnoreCase(editText.getText().toString()) == 0) {
                risultato = true;
            }
        }
        return risultato;
    }

    private void createPercorso() {
        Percorso percorso = new Percorso(editText.getText().toString(), dataBaseHelper.getIdLuogoCorrente());
        data.setIdPath(dataBaseHelper.addPercorso(percorso));

        if (data.getIdPath() != -1) {
            percorso.setId(data.getIdPath());

            //TODO controllo file vuoto
            ioHelper.listZoneSerializzazione(data.getData(), data.getIdPath());

        } else {
            Toast.makeText(this, "Si è verificato un errore! \n Riprova", Toast.LENGTH_SHORT).show();
        }
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
     * Metodo che inizializza la recycler view (griglia) e la vista
     */
    private void init() {
        if (data.getPathName() != null)
            editText.setText(data.getPathName());

        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

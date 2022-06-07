package com.example.eculturetool.activities.percorsi;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Permette la creazione di un nuovo percorso
 */
public class CreazionePercorsoActivity extends AppCompatActivity {


    @BindView(R.id.recyclerViewZone)
    RecyclerView recyclerView;

    EditText editText;
    MaterialButton btnConferma;
    MaterialButton btnAggiungiZona;

    DataBaseHelper dataBaseHelper;
    ItemTouchHelper itemTouchHelper;

    IoHelper ioHelper;

    //Recupero istanza del dataholder, contenente la lista di zone attualmente in uso per creare il percorso
    DataHolder data = DataHolder.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione_percorso);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        editText = findViewById(R.id.nomePercorso);
        btnConferma = findViewById(R.id.btnConfermaPercorso);
        btnAggiungiZona = findViewById(R.id.btnAggiungiZona);
        ImageView showTutorial = findViewById(R.id.showTutorialPercorsi);

        //Setting nome activity
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.creazione_percorso));

        //Creazione oggetto IoHelper, necessario per il salvataggio del percorso
        ioHelper = new IoHelper(getApplicationContext());

        //Recupero zone del luogo corrente
        data.setElencoZone((ArrayList<Zona>) dataBaseHelper.getZoneByIdLuogo(dataBaseHelper.getLuogoCorrente().getId()));
        //Recupero oggetti per ogni zona
        for (int i = 0; i < data.getElencoZone().size(); i++) {
            data.getElencoZone().get(i).addListaOggetti(dataBaseHelper.getOggettiByZona(data.getElencoZone().get(i)));
        }

        init();
        generateItems();

        //Setting bottone "Aggiungi zona"
        btnAggiungiZona.setOnClickListener(v -> {
            if (!editText.getText().toString().equals(""))
                data.setPathName(editText.getText().toString());

            finish();
            Intent intent = new Intent(this, AddZonaToPercorsoActivity.class);
            startActivity(intent);
        });

        //Setting bottone Tutorial
        showTutorial.setOnClickListener(v -> {
            //Intent intent = new Intent(this, ProvaTutorialActivity.class);
            //startActivity(intent);

            final Dialog dialog = new Dialog(CreazionePercorsoActivity.this);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_tutorial);
            dialog.show();

            VideoView videoView = dialog.findViewById(R.id.video_tutorialDialog);

            String videoPath="android.resource://"+getPackageName()+"/"+R.raw.video;

            Uri uri=Uri.parse(videoPath);
            videoView.setVideoURI(uri);

            MediaController mediaController= new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();
        });

        //Setting bottone "conferma" e controllo validità dati
        btnConferma.setOnClickListener(v -> {

            //Questo ciclo consente di aggiungere tutti gli oggetti alle zone del percorso (sia zone principali che diramazioni)
            for(Zona zona: data.getData()){
                zona.addListaOggetti(dataBaseHelper.getOggettiByZona(zona));
                for(Zona diramazione: zona.getDiramazione()){
                    diramazione.addListaOggetti(dataBaseHelper.getOggettiByZona(diramazione));
                }
            }

            if (data.getData().size() == 0) {
                Toast.makeText(this, getResources().getString(R.string.percorso_vuoto), Toast.LENGTH_SHORT).show();
                return;
            }

            if (editText.getText().toString().isEmpty()) {
                editText.setError(getString(R.string.inserisci_nome_percorso));
                editText.requestFocus();
                return;
            }

            if (data.getIdPath() != 0) {
                if (esistenzaNomePercorsoModifica()) {
                    editText.setError(getString(R.string.nome_esistente));
                    editText.requestFocus();
                    return;
                }

                dataBaseHelper.modificaPercorso(data.getIdPath(), editText.getText().toString());
                ioHelper.listZoneSerializzazione(data.getData(), data.getIdPath());

            } else {
                if (!checkNomePercorso())
                    return;

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

            //Pulizia dei dati dopo il salvataggio
            data.setIdPath(0);
            data.getData().clear();
            editText.getText().clear();
        });
    }


    /**
     * Effettua il controllo sulla validità del nome del percorso
     * @return true(se il nome non è già esisistente e non è vuoto), false altrimenti
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


    /**
     * Effettua il controllo sull'esistenza del nome del percorso
     * @return true se il nome è già esistente, false altrimenti
     */
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

    /**
     * Effettua il controllo sull'esistenza del nome del percorso, durante l'operazione di modifica
     * @return false se il nome non esiste o non è stato modificato, true altrimenti
     */
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

    /**
     * Permette la screazione di un nuovo oggetto Percorso
     */
    private void createPercorso() {
        Percorso percorso = new Percorso(editText.getText().toString(), dataBaseHelper.getIdLuogoCorrente());
        data.setIdPath(dataBaseHelper.addPercorso(percorso));

        if (data.getIdPath() != -1) {
            percorso.setId(data.getIdPath());

            //TODO controllo file vuoto
            ioHelper.listZoneSerializzazione(data.getData(), data.getIdPath());

        } else {
            Toast.makeText(this, getResources().getString(R.string.errore_percorso_riprova), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Permette la creazione delle cards zone, all'interno di una griglia ordinata
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

    /**
     * Alla pressione del tasto indietro l'activity termina
     */
    @Override
    public void onBackPressed() {
        finish();
    }

}

package com.example.eculturetool.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.ablanco.zoomy.Zoomy;
import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.view.GraphView;
import com.google.android.material.textfield.TextInputLayout;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;

public class RiepilogoPercorsoActivity extends AppCompatActivity {
    private Button eliminaBtn;
    private Button modificaBtn;
    DataHolder data = DataHolder.getInstance();
    private Graph<Zona, DefaultEdge> graph;
    private IoHelper ioHelper;
    private int idPercorso;
    private DataBaseHelper dataBaseHelper;
    private TextInputLayout spinner;
    private AutoCompleteTextView autoCompleteTextView;

    private List<String> zoneNomi = new ArrayList<>();
    private List<Oggetto> oggetti = new ArrayList<>();
    private List<Zona> zone;

    //TODO in tasto condividi genera eccezione

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo_percorso);
        modificaBtn = findViewById(R.id.modificaPercorso);
        eliminaBtn = findViewById(R.id.eliminaPercorso);

        ioHelper = new IoHelper(this);
        dataBaseHelper = new DataBaseHelper(this);

        Intent i = getIntent();
        graph = (Graph<Zona, DefaultEdge>) i.getExtras().getSerializable("grafo");
        idPercorso = i.getIntExtra("ID_PERCORSO", 0);

        GraphView graphView = findViewById(R.id.graphView);
        graphView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        graphView.setGrafo(graph);

        Toolbar myToolbar = findViewById(R.id.toolbarDettaglioPercorso);
        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        myToolbar.setTitle(dataBaseHelper.getPercorsoById(idPercorso).getNome());
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(view -> {
            //Ritorna al fragment del profilo chiamante
            finish();
        });

        scritturaSuFileJson();

        Zoomy.Builder builder = new Zoomy.Builder(this).target(graphView);
        builder.register();

        gestioneSpinner();
    }

    private void gestioneSpinner() {
        settaggioSpinner();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(Zona zona: zone){
                    if(zona.getNome().compareTo(zoneNomi.get(i)) == 0){
                        oggetti.addAll(zona.getListaOggetti());
                        break;
                    }

                    for(Zona zonaDiramazione: zona.getDiramazione()){
                        if(zonaDiramazione.getNome().compareTo(zoneNomi.get(i)) == 0){
                            oggetti.addAll(zonaDiramazione.getListaOggetti());
                            break;
                        }
                    }
                }

                String nomiOggetti = "\n";


                for(Oggetto oggetto: oggetti){
                    nomiOggetti = nomiOggetti + oggetto.getNome() + " \n\n";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(RiepilogoPercorsoActivity.this);
                builder.create();
                builder.setTitle("Oggetti nella zona " + "\"" + zoneNomi.get(i) + "\"").setMessage(nomiOggetti).show();
                oggetti.clear();
            }
        });
    }

    private void settaggioSpinner() {
        spinner = findViewById(R.id.materialSpinner);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        zone = ioHelper.listZoneDeserializzazione(idPercorso);
        zoneNomi = new ArrayList<>();
        oggetti = new ArrayList<>();

        for(Zona zona: zone){
            if(!zoneNomi.contains(zona.getNome())){
                zoneNomi.add(zona.getNome());
            }

            for(Zona zonaDiramazione: zona.getDiramazione()){
                if(!zoneNomi.contains(zonaDiramazione.getNome())){
                    zoneNomi.add(zonaDiramazione.getNome());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, zoneNomi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        autoCompleteTextView.setAdapter(adapter);
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

        item.setOnMenuItemClickListener(menuItem -> {
            ioHelper.shareFileTxt(idPercorso);
            return false;
        });

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        eliminaBtn.setOnClickListener(view -> showCustomDialog());

        modificaBtn.setOnClickListener(v -> {
            data.setData((ArrayList<Zona>) ioHelper.listZoneDeserializzazione(idPercorso));
            data.setPathName(dataBaseHelper.getPercorsoById(idPercorso).getNome());
            data.setIdPath(idPercorso);
            Intent intent = new Intent(RiepilogoPercorsoActivity.this, CreazionePercorsoActivity.class);
            intent.putExtra("ID_PERCORSO", idPercorso);
            startActivity(intent);
            finish();
        });
    }


    // Metodo che gestisce il dialog di conferma eliminazione del percorso
    void showCustomDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);

        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText("Vuoi cancellare definitivamente il percorso?");

        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();

        conferma.setOnClickListener(view -> {
            dataBaseHelper.deletePercorso(idPercorso);
            ioHelper.cancellaPercorsoArray(idPercorso);
            ioHelper.cancellaPercorsoJson(idPercorso);
            ioHelper.cancellaPercorso(idPercorso);
            dialog.dismiss();
            finish();
        });

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }
}
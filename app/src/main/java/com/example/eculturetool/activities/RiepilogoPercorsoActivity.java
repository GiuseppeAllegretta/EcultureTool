package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.database.SessionManagement;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.view.GraphView;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class RiepilogoPercorsoActivity extends AppCompatActivity {
    private GraphView graphView;
    private Button modificaBtn;
    private Button eliminaBtn;
    private Graph<Zona, DefaultEdge> graph;
    private Intent i;
    private IoHelper ioHelper;
    private int idPercorso; //id del percorso
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo_percorso);
        modificaBtn = findViewById(R.id.modificaPercorso);
        eliminaBtn = findViewById(R.id.eliminaPercorso);
        ioHelper = new IoHelper(this);
        dataBaseHelper = new DataBaseHelper(this);

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


        Zoomy.Builder builder = new Zoomy.Builder(this).target(graphView);

        builder.register();
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

    @Override
    protected void onStart() {
        super.onStart();

        eliminaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }


    /**
     * Metodo che gestisce il dialog di conferma eliminazione del profilo.
     * E' possibile confermare o rifiutare l'eliminazione del profilo attraverso gli appositi button
     */
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

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deletePercorso(idPercorso);
                ioHelper.cancellaPercorsoArray(idPercorso);
                ioHelper.cancellaPercorsoJson(idPercorso);
                ioHelper.cancellaPercorso(idPercorso);
                dialog.dismiss();
                finish();
            }
        });

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }
}
package com.example.eculturetool.activities.zone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterZona;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Zona;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ZoneActivity extends AppCompatActivity implements RecyclerAdapterZona.OnZonaListener {

    private String emailOspite = "admin@gmail.com"; //email dell'account ospite

    private ArrayList<Zona> zoneList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddLuogo;
    RecyclerAdapterZona adapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        fabAddLuogo = findViewById(R.id.addZona);

        Toolbar myToolbar = findViewById(R.id.toolbarZone);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(view -> {
            //Ritorna al fragment del profilo chiamante
            finish();
        });


        recyclerView = findViewById(R.id.recyclerViewZone);
        zoneList = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);

        setZoneInfo();

        nascondiView();

        if(zoneList.isEmpty()){
            showTutorial();
        }
    }

    // Questo metodo consente di nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite

    private void nascondiView() {
        dataBaseHelper = new DataBaseHelper(this);

        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        if (emailCuratore.compareTo(emailOspite) == 0) {
            fabAddLuogo.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //aggiungo una zona quando premo il fab mediante un intent esplicito che rimanda all'activity corrispondente
        fabAddLuogo.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AggiungiZonaActivity.class)));
    }

//imposto l'adapter per la recyclerView
    private void setAdapter() {
        adapter = new RecyclerAdapterZona(zoneList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
    //rimanda alla zona corrispondente quando seleziono una zona dalla recyclerView
    //e passa le informazioni della zona all'activity del dettaglio
    @Override
    public void onZonaClick(int position) {
        Zona z = zoneList.get(position);

        Intent intent = new Intent(this, DettaglioZonaActivity.class);

        Bundle b = new Bundle();
        b.putSerializable("ZONE", z);
        intent.putExtras(b);
        startActivity(intent);
    }
    // permette di eliminare la zona se effettuo un longpress
    @Override
    public void onZonaLongClick(int position) {
        showCustomDialog(position);
    }


    //metodo che effettua la ricerca di una specifica zona
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setZoneInfo();
    }
//recupero le informazioni delle zone mediante query
    private void setZoneInfo() {
        zoneList.clear();
        zoneList = dataBaseHelper.getZone();
        setAdapter();
    }

//visualizzo il dialog che permette di eliminare una zona
    private void showCustomDialog(int p) {
        final Dialog dialog = new Dialog(this);

        Zona zonaEliminare = zoneList.get(p);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);

        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText(getResources().getString(R.string.cancella_zona));
        TextView descrizione_tv = layout.findViewById(R.id.descrizione_dialog);
        descrizione_tv.setText(getResources().getString(R.string.NB_zona));


        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();
        //confermo l'eliminazione di una zona
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.rimuoviZona(zonaEliminare);
                zoneList.remove(p);
                dialog.dismiss();
                finish();
                startActivity(getIntent());

            }
        });
        //annullo l'eliminazione di una zona
        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }

//visualizzo il tutorial per l'activity corrispondente
    private void showTutorial(){
        TapTargetView.showFor(this,
                TapTarget.forView(fabAddLuogo, getString(R.string.aggiungi_zona), getString(R.string.zona_msg_1)+ "\n" +
                        getString(R.string.zona_msg_2))
                        // All options below are optional
                        .outerCircleColor(R.color.gialloSecondario)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(15)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(12)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(false)
                        .transparentTarget(true)
                        .targetRadius(60),
                new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                    }
                });

    }
}

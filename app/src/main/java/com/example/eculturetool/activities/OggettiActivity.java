package com.example.eculturetool.activities;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterOggetto;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OggettiActivity extends AppCompatActivity implements RecyclerAdapterOggetto.OnOggettoListener {

    private String emailOspite = "admin@gmail.com"; //email dell'account ospite

    private DataBaseHelper dataBaseHelper;
    private ArrayList<Oggetto> oggettiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapterOggetto adapter;
    private LinearLayout layoutNoZone;

    private List<Zona> zoneList = new ArrayList<>();
    private FloatingActionButton fabAddOggetto;
    private Button addZona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oggetti);
        fabAddOggetto = findViewById(R.id.addOggetto);
        recyclerView = findViewById(R.id.recyclerViewOggetti);
        layoutNoZone = findViewById(R.id.layout_no_zone);
        addZona = findViewById(R.id.addZonaNoZone);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarOggetti);
        dataBaseHelper = new DataBaseHelper(this);

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
        layoutNoZone.setVisibility(View.INVISIBLE);
        retrieveZone();

        setOggettoInfo();

        if (zoneList.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            fabAddOggetto.setVisibility(View.INVISIBLE);
            layoutNoZone.setVisibility(View.VISIBLE);


        } else if (zoneList.size() != 0 && oggettiList.isEmpty()) {
            showTutorial();
        }
        //setOggettoInfo();

        //setAdapter();


        //metodo che nasconde le view in caso di accesso con account ospite
        nascondiView();


    }


    /**
     * Questo metodo consente di nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite
     */
    private void nascondiView() {
        dataBaseHelper = new DataBaseHelper(this);

        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        if (emailCuratore.compareTo(emailOspite) == 0) {
            fabAddOggetto.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Metodo che recupera le zone
     */
    private void retrieveZone() {
        zoneList.clear();
        zoneList = dataBaseHelper.getZone();
    }


    @Override
    protected void onStart() {
        super.onStart();

        fabAddOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiOggettoActivity.class));
            }
        });

        addZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiZonaActivity.class));
                finish();
            }
        });

    }

    private void setAdapter() {
        adapter = new RecyclerAdapterOggetto(oggettiList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOggettoInfo() {
        dataBaseHelper = new DataBaseHelper(this);
        oggettiList.clear();
        oggettiList = (ArrayList<Oggetto>) dataBaseHelper.getOggetti();

        setAdapter();
    }


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
        return true;
    }


    @Override
    public void onOggettoClick(int position) {
        int oggettoSelezionato = oggettiList.get(position).getId();

        Intent intent = new Intent(this, DettaglioOggettoActivity.class);
        intent.putExtra(Oggetto.Keys.ID, oggettoSelezionato);
        intent.putExtra("ZONELIST", (Serializable) zoneList);
        startActivity(intent);
    }

    @Override
    public void onOggettoLongClick(int position) {
        showCustomDialog(position);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setOggettoInfo();
        retrieveZone();
        setAdapter();
    }

    void showCustomDialog(int p) {
        final Dialog dialog = new Dialog(this);
        int idOggettoEliminare = oggettiList.get(p).getId();


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_elimina_oggetto);

        final Button conferma = dialog.findViewById(R.id.conferma_cancellazione_oggetto);
        final Button rifiuto = dialog.findViewById(R.id.annulla_cancellazione_oggetto);

        dialog.show();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.deleteOggetto(idOggettoEliminare);
                oggettiList.remove(p);
                dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }

    private void showTutorial() {


        TapTargetView.showFor(this,
                TapTarget.forView(fabAddOggetto, getString(R.string.aggiungi_oggetto), getString(R.string.Aggiungi_da_qui) + "\n" + getString(R.string.un_nuovo_oggetto))
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
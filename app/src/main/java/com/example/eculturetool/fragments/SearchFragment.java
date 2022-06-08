package com.example.eculturetool.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.oggetti.DettaglioOggettoActivity;
import com.example.eculturetool.activities.percorsi.RiepilogoPercorsoActivity;
import com.example.eculturetool.activities.zone.DettaglioZonaActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.IoHelper;
import com.example.eculturetool.entities.Entita;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Percorso;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterEntita;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe che si occupa di gestire il fragment relativo alla ricerca globale rinvenibile nel menu della home
 */
public class SearchFragment extends Fragment implements RecyclerAdapterEntita.OnEntitaListener {

    /**
     * Lista che contiene oggetti di tipo Entità i cui figli sono Oggetti, Zone, Luoghi e Percorsi
     */
    private ArrayList<Entita> entitaList;
    private RecyclerView recyclerView;
    private RecyclerAdapterEntita adapterEntita;
    private DataBaseHelper dataBaseHelper;

    private Button oggettiBtn;
    private Button zoneBtn;
    private Button percorsiBtn;


    public SearchFragment() {
        //Richiesto costruttore pubblico vuoto
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /**
     * Metodo privato che va a settare l'adapter che conterrà tutti oggetti di tipo entità
     */
    private void setAdapter() {
        adapterEntita = new RecyclerAdapterEntita(entitaList, this);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //Setta l'adapter all'interno della recyclerView con i relativi elementi
        recyclerView.setAdapter(adapterEntita);
    }

    /**
     * Metoto che gestisce il caricamento degli elementi nel momento in cui si va a selezionare uno dei tre bottoni (oggetti, percorsi, zone). Inoltre gestisce gli stati quando
     * questo sono cliccati
     */
    private void setEntitaInfo() {
        dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());

        entitaList.clear();
        //recupero dei dati dal DB e assegnamento della lista alla variabile
        entitaList.addAll(dataBaseHelper.getOggetti());
        //il bottone oggetti è selezionato di default
        oggettiBtn.setSelected(true);
        zoneBtn.setSelected(false);
        percorsiBtn.setSelected(false);

        //Metodo che si occupa delle operazioni per settare l'adapter
        setAdapter();

        //operazioni da eseguire quando si clicca sul pulsante oggetti. Si occupa del caricamente e gestione dello stato del pulsante
        oggettiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getOggetti());
                oggettiBtn.setSelected(true);
                zoneBtn.setSelected(false);
                percorsiBtn.setSelected(false);
                //Metodo che si occupa delle operazioni per settare l'adapter con gli elementi relativi agli oggetti
                setAdapter();
            }
        });

        //operazioni da eseguire quando si clicca sul pulsante zone. Si occupa del caricamente e gestione dello stato del pulsante
        zoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getZone());
                oggettiBtn.setSelected(false);
                zoneBtn.setSelected(true);
                percorsiBtn.setSelected(false);
                //Metodo che si occupa delle operazioni per settare l'adapter con gli oggetti relativi alle zone
                setAdapter();
            }
        });

        //operazioni da eseguire quando si clicca sul pulsante percorsi. Si occupa del caricamente e gestione dello stato del pulsante
        percorsiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getPercorsi());
                oggettiBtn.setSelected(false);
                zoneBtn.setSelected(false);
                percorsiBtn.setSelected(true);
                //Metodo che si occupa delle operazioni per settare l'adapter con gli oggetti relativi ai percorsi
                setAdapter();
            }
        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recupero dei riferimenti delle view
        recyclerView = view.findViewById(R.id.recyclerViewEntita);
        oggettiBtn = view.findViewById(R.id.oggettiSrc);
        zoneBtn = view.findViewById(R.id.ZoneSrc);
        percorsiBtn = view.findViewById(R.id.percorsiSrc);

        entitaList = new ArrayList<>();
        setEntitaInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        //operazioni che settano la Toolbar nel fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarEntita);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        return view;
    }


    /**
     * Metoto che gestisce il menu delle opzioni presenti nella toolbar nello specifico la lente di ingrandimento che permette di effettuare la ricerca
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflate del menu
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.ricerca);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //azioni da eseguire nel caso in cui si effettua al ricerca
                adapterEntita.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Metodo che gestisce il click su uno degli elementi della recyclerView
     * @param position l'indice dell'elemento cliccato
     */
    @Override
    public void onEntitaClick(int position) {

        //OPeraziomni da eseguire nel caso in cui si dovesse cliccare su un elemento della recycler che sia un Oggetto
        if (entitaList.get(position) instanceof Oggetto) {
            //recuper dell'id dell'oggetto selezionato
            int oggettoSelezionato = entitaList.get(position).getId();
            ArrayList<Zona> zoneList = dataBaseHelper.getZone();

            //OPERAZIONI CHE CONSENTONO DI AVVIARE L'ACITIVITY DI ARRIVO
            Intent intent = new Intent(getActivity(), DettaglioOggettoActivity.class);
            intent.putExtra(Oggetto.Keys.ID, oggettoSelezionato);
            intent.putExtra("ZONELIST", (Serializable) zoneList);
            startActivity(intent);

            //OPeraziomni da eseguire nel caso in cui si dovesse cliccare su un elemento della recycler che sia una Zona
        } else if (entitaList.get(position) instanceof Zona) {
            Zona z = (Zona) entitaList.get(position);

            Intent intent = new Intent(getActivity(), DettaglioZonaActivity.class);

            Bundle b = new Bundle();
            b.putSerializable("ZONE", z);
            intent.putExtras(b);
            startActivity(intent);

            //OPeraziomni da eseguire nel caso in cui si dovesse cliccare su un elemento della recycler che sia un Percorso
        } else if (entitaList.get(position) instanceof Percorso) {

            IoHelper ioHelper = new IoHelper(getActivity().getApplicationContext());

            int idPercorso = entitaList.get(position).getId();
            Graph<Zona, DefaultEdge> graph = ioHelper.deserializzaPercorso(idPercorso);
            Intent intent = new Intent(getActivity(), RiepilogoPercorsoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID_PERCORSO", idPercorso);
            bundle.putSerializable("grafo", (Serializable) graph);

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setEntitaInfo();
    }
}
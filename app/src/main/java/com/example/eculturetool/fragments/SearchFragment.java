package com.example.eculturetool.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.oggetti.DettaglioOggettoActivity;
import com.example.eculturetool.activities.zone.DettaglioZonaActivity;
import com.example.eculturetool.percorsi.RiepilogoPercorsoActivity;
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


public class SearchFragment extends Fragment implements RecyclerAdapterEntita.OnEntitaListener {

    private ArrayList<Entita> entitaList;
    private RecyclerView recyclerView;
    private RecyclerAdapterEntita adapterEntita;
    private DataBaseHelper dataBaseHelper;

    private Button oggettiBtn;
    private Button zoneBtn;
    private Button percorsiBtn;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void setAdapter() {
        adapterEntita = new RecyclerAdapterEntita(entitaList, this);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterEntita);
    }

    private void setEntitaInfo() {
        dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());

        entitaList.clear();
        entitaList.addAll(dataBaseHelper.getOggetti());
        oggettiBtn.setSelected(true);
        zoneBtn.setSelected(false);
        percorsiBtn.setSelected(false);
        setAdapter();

        oggettiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getOggetti());
                oggettiBtn.setSelected(true);
                zoneBtn.setSelected(false);
                percorsiBtn.setSelected(false);
                setAdapter();
            }
        });

        zoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getZone());
                oggettiBtn.setSelected(false);
                zoneBtn.setSelected(true);
                percorsiBtn.setSelected(false);
                setAdapter();
            }
        });

        percorsiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getPercorsi());
                oggettiBtn.setSelected(false);
                zoneBtn.setSelected(false);
                percorsiBtn.setSelected(true);
                setAdapter();
            }
        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewEntita);
        oggettiBtn = view.findViewById(R.id.oggettiSrc);
        zoneBtn = view.findViewById(R.id.ZoneSrc);
        percorsiBtn = view.findViewById(R.id.percorsiSrc);

        entitaList = new ArrayList<>();
        setEntitaInfo();
        //setAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarEntita);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
                adapterEntita.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onEntitaClick(int position) {

        if (entitaList.get(position) instanceof Oggetto) {
            int oggettoSelezionato = entitaList.get(position).getId();
            ArrayList<Zona> zoneList = dataBaseHelper.getZone();

            Intent intent = new Intent(getActivity(), DettaglioOggettoActivity.class);
            intent.putExtra(Oggetto.Keys.ID, oggettoSelezionato);
            intent.putExtra("ZONELIST", (Serializable) zoneList);
            startActivity(intent);

        } else if (entitaList.get(position) instanceof Zona) {
            Zona z = (Zona) entitaList.get(position);

            Intent intent = new Intent(getActivity(), DettaglioZonaActivity.class);

            Bundle b = new Bundle();
            b.putSerializable("ZONE", z);
            intent.putExtras(b);
            startActivity(intent);

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
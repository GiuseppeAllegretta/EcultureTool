package com.example.eculturetool.fragments;

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
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Entita;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterEntita;

import java.util.ArrayList;
import java.util.Objects;


public class SearchFragment extends Fragment {

    private ArrayList<Entita> entitaList;
    private RecyclerView recyclerView;
    private RecyclerAdapterEntita adapterEntita;

    private Button oggettiBtn;
    private Button zoneBtn;
    private Button percorsiBtn;


    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    private void setAdapter() {
        adapterEntita= new RecyclerAdapterEntita(entitaList);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterEntita);
    }

    private void setEntitaInfo() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());

        //ntitaList.addAll(dataBaseHelper.getOggetti());
        //entitaList.addAll(dataBaseHelper.getZone());
        //entitaList.addAll(dataBaseHelper.getPercorsi());

        oggettiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getOggetti());

                setAdapter();
            }
        });

        zoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getZone());
                setAdapter();
            }
        });

        percorsiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entitaList.clear();
                entitaList.addAll(dataBaseHelper.getPercorsi());
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

        entitaList= new ArrayList<>();
        setEntitaInfo();
        //setAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarEntita);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
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


}
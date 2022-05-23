package com.example.eculturetool.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Entita;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.utility_percorsi.RecyclerAdapterEntita;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private ArrayList<Entita> entitaList;
    private RecyclerView recyclerView;


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
        super.onCreate(savedInstanceState);


    }

    private void setAdapter() {
        RecyclerAdapterEntita adapterEntita= new RecyclerAdapterEntita(entitaList);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterEntita);
    }

    private void setEntitaInfo() {
        entitaList.add(new Luogo("l1","dl1", Tipologia.AREA_ARCHEOLOGICA,"ciaocurat"));
        entitaList.add(new Luogo("l2","dl2", Tipologia.SITO_CULTURALE,"ciaocurat"));
        entitaList.add(new Luogo("l3","dl3", Tipologia.MOSTRA_ITINERANTE,"ciaocurat"));
        entitaList.add(new Oggetto(10,"telefono","chiamare",""));
        entitaList.add(new Oggetto(1,"telefono1","chiamare1",""));
        entitaList.add(new Oggetto(13,"telefono2","chiamare2",""));

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewEntita);
        entitaList= new ArrayList<>();
        setEntitaInfo();
        setAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
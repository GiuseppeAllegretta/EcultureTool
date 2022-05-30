package com.example.eculturetool.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterLuogo;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Luogo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LuoghiActivity extends AppCompatActivity implements RecyclerAdapterLuogo.OnLuogoListener {

    private DataBaseHelper dataBaseHelper;
    private ArrayList<Luogo> luoghiList;
    private RecyclerView recyclerView;
    private RecyclerAdapterLuogo adapter;
    private FloatingActionButton fabAddLuogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luoghi);
        fabAddLuogo = findViewById(R.id.addLuogo);

        Toolbar myToolbar = findViewById(R.id.toolbarLuoghi);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(view -> {
            //Ritorna al fragment del profilo chiamante
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);
        luoghiList = new ArrayList<>();

        setLuogoInfo();
        nascondiView();
    }

    /**
     * Questo metodo consente di nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite
     */
    private void nascondiView() {
        dataBaseHelper = new DataBaseHelper(this);

        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        //email dell'account ospite
        String emailOspite = "admin@gmail.com";
        if(emailCuratore.compareTo(emailOspite) == 0){
            fabAddLuogo.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        fabAddLuogo.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AggiungiLuogoActivity.class)));
    }

    private void setAdapter() {
        adapter = new RecyclerAdapterLuogo(luoghiList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setLuogoInfo() {
        dataBaseHelper = new DataBaseHelper(this);
        luoghiList.clear();
        luoghiList = (ArrayList<Luogo>) dataBaseHelper.getLuoghi();
        setAdapter();
    }


    @Override
    public void onLuogoClick(int position) {
        int luogoSelezionato = luoghiList.get(position).getId();
        Intent intent = new Intent(this, DettaglioLuogoActivity.class);
        intent.putExtra(Luogo.Keys.ID, luogoSelezionato);
        startActivity(intent);
    }

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
        setLuogoInfo();
    }
}
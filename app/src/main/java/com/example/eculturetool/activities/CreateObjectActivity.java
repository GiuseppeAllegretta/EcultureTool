package com.example.eculturetool.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.RecyclerAdapterLuogo;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

public class CreateObjectActivity extends AppCompatActivity implements RecyclerAdapterLuogo.OnLuogoListener {


    private final Connection connection = new Connection();

    private ArrayList<Luogo> luoghiList;
    private RecyclerView recyclerView;

    private String luogoCorrente;
    private RecyclerAdapterLuogo adapter;

    private FloatingActionButton fabAddLuogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luoghi);
        fabAddLuogo = findViewById(R.id.addLuogo);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarLuoghi);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        //myToolbar.setNavigationIcon(freccia_indietro);
        //setSupportActionBar(myToolbar);

        //Azione da eseguire quando si clicca la freccia di navigazione
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ritorna al fragment del profilo chiamante
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        luoghiList = new ArrayList<>();

        setLuogoInfo();
    }


    @Override
    protected void onStart() {
        super.onStart();

        fabAddLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AggiungiLuogoActivity.class));
            }
        });

    }

    private void setAdapter() {
        adapter = new RecyclerAdapterLuogo(luoghiList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setLuogoInfo() {
        connection.getRefCuratore().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Curatore.class) != null) {

                    //Ottengo il luogo corrente del curatore
                    luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();

                    connection.getRefLuogo().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                            int count = (int) snapshot.getChildrenCount();
                            System.out.println("count: " + count);

                            luoghiList.clear();
                            for (int i = 0; i < count; i++) {
                                luoghiList.add(iteratore.iterator().next().getValue(Luogo.class));
                                //Luogo luogoprova= new Luogo("scavo","ciao", Tipologia.SITO_CULTURALE,Connection.getUidCuratore());
                                //luoghiList.add(luogoprova);
                                System.out.println(luoghiList.get(i));
                            }
                            setAdapter();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onLuogoClick(int position) {
        String luogoSelezionato = luoghiList.get(position).getId();
        Intent intent = new Intent(this, DettaglioLuogoActivity.class);
        intent.putExtra("LUOGO", luogoSelezionato);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.oggetti_menu, menu);
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

}
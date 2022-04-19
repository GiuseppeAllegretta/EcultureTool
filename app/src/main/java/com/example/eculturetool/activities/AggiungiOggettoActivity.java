package com.example.eculturetool.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.fragments.DialogAddOggettoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AggiungiOggettoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Connection connection = new Connection();

    public static final String OBJECTS_IMAGES_DIR = "objects_images";
    protected static Curatore curatore;

    private EditText nomeOggetto, descrizioneOggetto;
    private Spinner tipologiaOggetto;
    private Button creaOggetto;
    private ProgressBar progressBar;
    private String luogoCorrente;
    private ImageView imgOggetto;
    private ActivityResultLauncher<Intent> startForObjectImageUpload;
    private Uri imgUri;
    private TipologiaOggetto tipologia;

    //Si recupera questa lista per fare in modo che l'utente non crei un oggetto con lo stesso nome uno precedente
    List<Oggetto> oggettiList = new ArrayList<>();

    //VARIABILI GESTIONE SPINNER PER LE ZONE
    private Spinner spinnerZone;
    private List<String> nomiZoneList = new ArrayList<>();
    private List<Zona> zoneList = new ArrayList<>();
    private ArrayAdapter<String> nomiZoneListAdapter;
    private int countZone;
    private String zonaSelezionata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_oggetti);

        imgOggetto = findViewById(R.id.imgOggetto);
        nomeOggetto = findViewById(R.id.nome_oggetto_add);
        descrizioneOggetto = findViewById(R.id.descrizione_oggetto_add);
        tipologiaOggetto = findViewById(R.id.spinner_tipologia_oggetto_add);
        creaOggetto = findViewById(R.id.creaOggetto);
        progressBar = findViewById(R.id.progressAddOggetto);
        FloatingActionButton changeImg = findViewById(R.id.change_imgUser);
        spinnerZone = findViewById(R.id.spinner_zona_add);


        startForObjectImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        imgUri = uri;
                        Glide.with(this).load(imgUri).circleCrop().into(imgOggetto);
                    }
                });

        changeImg.setOnClickListener(onClickListener -> {
            Intent uploadImageIntent = new Intent(this, UploadImageActivity.class);
            uploadImageIntent.putExtra("directory", OBJECTS_IMAGES_DIR);
            startForObjectImageUpload.launch(uploadImageIntent);
        });

        setZoneSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Queste operazioni consentono di recuperare tutti gli oggetti che appartengono a uno stesso luogo
        connection.getRefCuratore().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Curatore.class) != null) {
                    //luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();
                    oggettiList = getListOggettiCreati();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_oggetti, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tipologiaOggetto.setAdapter(adapter);
        tipologiaOggetto.setOnItemSelectedListener(this);
        creaOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creazioneOggetto();
            }
        });
    }


    /**
     * Metodo per settare l'elenco delle zone nello spinner relativo. Per ora ci sono solo valori statici
     */
    private void setZoneSpinner() {
        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    luogoCorrente = snapshot.getValue(String.class).toString();

                    connection.getRefZone().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(Zona.class) != null) {
                                Iterable<DataSnapshot> iteratoreZone = snapshot.getChildren();
                                countZone = (int) snapshot.getChildrenCount();

                                zoneList.clear(); //pulisce la lista prima di riempirla
                                //Avvalora zoneList con tutte le zone prese da db
                                for (int i = 0; i < countZone; i++) {
                                    zoneList.add(iteratoreZone.iterator().next().getValue(Zona.class));
                                }

                                nomiZoneList.clear(); //pulisce la lista prima di riempirla
                                //Avvalora nomiZoneList con i nomi di ogni singola zona
                                for (int i = 0; i < countZone; i++) {
                                    nomiZoneList.add(zoneList.get(i).getNome());
                                }

                                nomiZoneListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, nomiZoneList);
                                spinnerZone.setAdapter(nomiZoneListAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

                spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        zonaSelezionata = nomiZoneList.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void openDialog() {
        DialogAddOggettoFragment dialogAddOggettoFragment = new DialogAddOggettoFragment();
        dialogAddOggettoFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void creazioneOggetto() {
        if (countZone == 0) {
            openDialog();
            return;
        }

        String nome = nomeOggetto.getText().toString().trim();
        String descrizione = descrizioneOggetto.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeOggetto.setError(getResources().getString(R.string.nome_oggetto_richiesto));
            nomeOggetto.requestFocus();
            return;
        }

        if (controlloEsistenzaNomeOggetto(nome)) {
            nomeOggetto.requestFocus();
            nomeOggetto.setError(getResources().getString(R.string.nome_esistente));
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneOggetto.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneOggetto.requestFocus();
            return;
        }

        if (tipologiaOggetto == null) {
            tipologiaOggetto.requestFocus();
            return;
        }

        if(imgUri == null){
            Toast.makeText(this, getResources().getString(R.string.inserimento_immagine), Toast.LENGTH_LONG).show();
            return;
        }

        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        //Scrittura dell'oggetto sul Realtime Database
        String key = connection.getRefOggetti().push().getKey();
        //Oggetto oggetto = new Oggetto(key, nome, descrizione, imgUri.toString(), tipologia, zonaSelezionata);


        //Metodo che recupare l'id della zona
        String idZona = retrieveZona(zonaSelezionata);

        //connection.getRefOggetti().child(luogoCorrente).child(idZona).child(key).setValue(oggetto);

        //La progressbar diventa visibile
        progressBar.setVisibility(View.INVISIBLE);

        finish();
    }

    /**
     * Metodo che recupara l'id della zona selezionata dall'utente in fase di creazione dell'oggetto
     *
     * @param zonaSelezionata: parametro Stringa che rappresenta il nome della zona selezionata nello spinner
     * @return id della zona (firebase)
     */
    private String retrieveZona(String zonaSelezionata) {
        String risultato = null;
/*
        for (Zona zona : zoneList) {
            if (zona.getNome().compareTo(zonaSelezionata) == 0) {
                risultato = zona.getId();
            }
        }

 */
        return risultato;
    }


    private boolean controlloEsistenzaNomeOggetto(String nomeOggetto) {
        boolean isEsistente = false;
        nomeOggetto = this.nomeOggetto.getText().toString();

        for (int i = 0; i < oggettiList.size(); i++) {
            if (nomeOggetto.compareToIgnoreCase(oggettiList.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }
        return isEsistente;
    }


    /**
     * @return: ritorna la lista degli oggetti memorizzati su firebase in riferimento a un determinato curatore tranne quello che si è selezionato
     */
    private List<Oggetto> getListOggettiCreati() {
        List<Oggetto> oggetti = new ArrayList<>();
        /*

        connection.getRefOggetti().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Zona zona : zoneList) {
                    Iterable<DataSnapshot> iteratore = snapshot.child(zona.getId()).getChildren();
                    int count = (int) snapshot.child(zona.getId()).getChildrenCount();

                    for (int i = 0; i < count; i++) {
                        oggetti.add(iteratore.iterator().next().getValue(Oggetto.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

         */
        return oggetti;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        switch (item) {
            case Oggetto.KeysTipologiaOggetto.QUADRO:
                tipologia = TipologiaOggetto.QUADRO;
                break;

            case Oggetto.KeysTipologiaOggetto.STATUA:
                tipologia = TipologiaOggetto.STATUA;
                break;

            case Oggetto.KeysTipologiaOggetto.SCULTURA:
                tipologia = TipologiaOggetto.SCULTURA;
                break;

            case Oggetto.KeysTipologiaOggetto.ALTRO:
                tipologia = TipologiaOggetto.ALTRO;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
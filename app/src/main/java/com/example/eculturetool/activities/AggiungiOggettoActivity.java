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

    private EditText nomeOggetto, descrizioneOggetto;
    private Spinner tipologiaOggetto;
    private Button creaOggetto;
    private ProgressBar progressBar;
    private String luogoCorrente;
    protected static Curatore curatore;
    private ImageView imgOggetto;
    private ActivityResultLauncher<Intent> startForObjectImageUpload;
    private Uri imgUri;
    private TipologiaOggetto tipologia;

    //Si recupera questa lista per fare in modo che l'utente non crei un oggetto con lo stesso nome uno precedente
    List<Oggetto> oggettiList = new ArrayList<>();

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
            System.out.println("Button Clicked");
            Intent uploadImageIntent = new Intent(this, UploadImageActivity.class);
            uploadImageIntent.putExtra("directory", OBJECTS_IMAGES_DIR);
            startForObjectImageUpload.launch(uploadImageIntent);
        });

        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null){
                    luogoCorrente = snapshot.getValue(String.class).toString();
                    oggettiList = getListOggettiCreati();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    private void creazioneOggetto() {

        String nome = nomeOggetto.getText().toString().trim();
        String descrizione = descrizioneOggetto.getText().toString().trim();

        if (nome.isEmpty()) {
            nomeOggetto.setError("Il nome dell'oggetto è richiesto");
            nomeOggetto.requestFocus();
            return;
        }

        if (controlloEsistenzaNomeOggetto(nome)) {
            nomeOggetto.requestFocus();
            nomeOggetto.setError("Nome già esistente");
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneOggetto.setError("La descrizione è richiesta");
            descrizioneOggetto.requestFocus();
            return;
        }

        System.out.println("tipologia: " + tipologiaOggetto);
        if (tipologiaOggetto == null) {
            tipologiaOggetto.requestFocus();
            return;
        }


        //La progressbar diventa visibile
        progressBar.setVisibility(View.VISIBLE);

        //Scrittura del luogo sul Realtime Database
        String key = connection.getRefOggetti().push().getKey();
        Oggetto oggetto = new Oggetto(key, nome, descrizione, imgUri.toString());

        connection.getRefCuratore().child("luogoCorrente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    String luogo = snapshot.getValue(String.class);
                    if (luogo != null) {
                        luogoCorrente = luogo;
                        if (key != null) {
                            connection.getRefOggetti().child(luogoCorrente).child(key).setValue(oggetto);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //La progressbar diventa visibile
        progressBar.setVisibility(View.INVISIBLE);

        finish();

    }


    private boolean controlloEsistenzaNomeOggetto(String nomeOggetto) {
        boolean isEsistente = false;
        nomeOggetto = this.nomeOggetto.getText().toString();

        for (int i = 0; i < oggettiList.size(); i++) {
            if (nomeOggetto.compareToIgnoreCase(oggettiList.get(i).getNome()) == 0) {
                //System.out.println("nome corrente: " + luoghiList.get(i).getNome());
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

        System.out.println("Luogo corrente" + luogoCorrente);
        connection.getRefOggetti().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> iteratore = snapshot.getChildren();
                int count = (int) snapshot.getChildrenCount();
                System.out.println("count: " + count);

                Oggetto oggetto;

                for (int i = 0; i < count; i++) {
                    oggetti.add(iteratore.iterator().next().getValue(Oggetto.class));
                }
                System.out.println(oggetti);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return oggetti;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        System.out.println("entra in onItemSelected");
        String item = adapterView.getItemAtPosition(i).toString();
        System.out.println("item: " + item);

        switch (item) {
            case "Quadro":
                tipologia = TipologiaOggetto.QUADRO;
                break;

            case "Statua":
                tipologia = TipologiaOggetto.STATUA;
                break;

            case "Scultura":
                tipologia = TipologiaOggetto.SCULTURA;
                break;

            case "Sito culturale":
                tipologia = TipologiaOggetto.ALTRO;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
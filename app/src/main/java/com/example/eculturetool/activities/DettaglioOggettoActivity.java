package com.example.eculturetool.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DettaglioOggettoActivity extends AppCompatActivity {

    public static final String OBJECTS_IMAGES_DIR = "objects_images";

    private DataBaseHelper dataBaseHelper;          //riferimento del database
    private Oggetto oggetto;                        //oggetto di cui si vedono i dati a schermo
    private List<Zona> zoneList;                    //L'insieme di tutte le zone
    private int idOggetto, idZona, luogoCorrente;   //identificativi dell'oggetto, zona e luogo

    private ActivityResultLauncher<Intent> startForObjectImageUpload;
    private TextView nomeOggetto, descrizioneOggetto, tipologiaOggetto, zonaAppartenenza;
    private ImageView immagineOggetto;
    private FloatingActionButton cambiaImmagine, modificaOggetto;
    private Toolbar myToolbar;
    private Button eliminaOggetto;
    private Context context;
    private Uri imgUri;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_oggetto);

        myToolbar = (Toolbar) findViewById(R.id.toolbarOggetto);

        //Operazione che consente di aggiungere una freccia di navigazione alla toolbar da codice
        Drawable freccia_indietro = ContextCompat.getDrawable(this, R.drawable.ic_freccia_back);
        myToolbar.setNavigationIcon(freccia_indietro);
        setSupportActionBar(myToolbar);

        //Avvaloro context
        context = getApplicationContext();

        //Ottengo i riferimenti alle View
        nomeOggetto = findViewById(R.id.nomeOggettoDettaglio);
        descrizioneOggetto = findViewById(R.id.descrizioneOggettoDettaglio);
        tipologiaOggetto = findViewById(R.id.tipologiaOggettoDettaglio);
        zonaAppartenenza = findViewById(R.id.zonaAppartenenza);
        immagineOggetto = findViewById(R.id.immagineOggetto);
        cambiaImmagine = findViewById(R.id.change_imgObject);
        modificaOggetto = findViewById(R.id.editOggetto);
        eliminaOggetto = findViewById(R.id.eliminaOggetto);
        progressBar = findViewById(R.id.progress);

        //Metodo di scroll per la textView
        descrizioneOggetto.setMovementMethod(new ScrollingMovementMethod());


        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        idOggetto = intent.getIntExtra(Oggetto.Keys.ID, 0);     //id dell'oggetto selezionato dalla recyclerView
        luogoCorrente = intent.getIntExtra(Luogo.Keys.ID, 0);
        zoneList = (List<Zona>) intent.getSerializableExtra("ZONELIST");


        /*startForObjectImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        imgUri = uri;
                        connection.getRefOggetti().child(luogoCorrente).child(idZona).child(idOggetto).child("url").setValue(imgUri.toString());
                        Glide.with(this).load(imgUri).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.VISIBLE);
                                return false;
                            }
                        }).circleCrop().into(immagineOggetto);
                    }
                });*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        setDatiOggetto();

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eliminaOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }

        });

        modificaOggetto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModificaOggettoActivity.class);
                intent.putExtra(Luogo.Keys.ID, luogoCorrente);
                intent.putExtra(Oggetto.Keys.ID, idOggetto);
                intent.putExtra(Zona.Keys.ID, idZona);
                startActivity(intent);
            }
        });

        cambiaImmagine.setOnClickListener(onClickListener -> {
            Intent uploadImageIntent = new Intent(this, UploadImageActivity.class);
            uploadImageIntent.putExtra("directory", OBJECTS_IMAGES_DIR);
            startForObjectImageUpload.launch(uploadImageIntent);
        });

    }

    private void setDatiOggetto() {
        dataBaseHelper = new DataBaseHelper(this);
        oggetto = dataBaseHelper.getOggettoById(idOggetto);

        if(oggetto != null){
            Glide.with(context).load(oggetto.getUrl()).circleCrop().into(immagineOggetto);
            getSupportActionBar().setTitle(oggetto.getNome());
            nomeOggetto.setText(oggetto.getNome());
            descrizioneOggetto.setText(oggetto.getDescrizione());
            tipologiaOggetto.setText(setTipologia(oggetto.getTipologiaOggetto()));
            zonaAppartenenza.setText(getNomeZona(oggetto.getZonaAppartenenza()));
            idZona = oggetto.getZonaAppartenenza();
        }
    }

    /**
     * Metodo che recupera il nome di una zona in base all'id della zona fornito in input
     * @param id id della zona in cui è contenuto l'oggetto
     * @return nomeZona cioè il nome di una zona sotto forma di stringa
     */
    private String getNomeZona(int id){
        String nomeZona = null;

        for(Zona zona: zoneList){
            if(zona.getId() == id){
                nomeZona = zona.getNome();
            }
        }
        return nomeZona;
    }

    private String setTipologia(TipologiaOggetto tipologiaOggetto) {
        String risultato = null;

        switch (tipologiaOggetto) {
            case QUADRO:
                risultato = Oggetto.KeysTipologiaOggetto.QUADRO;
                break;
            case STATUA:
                risultato = Oggetto.KeysTipologiaOggetto.STATUA;
                break;
            case SCULTURA:
                risultato = Oggetto.KeysTipologiaOggetto.SCULTURA;
                break;
            case ALTRO:
                risultato = Oggetto.KeysTipologiaOggetto.ALTRO;
                break;
        }
        return risultato;
    }


    void showCustomDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_elimina_oggetto);

        final Button conferma = dialog.findViewById(R.id.conferma_cancellazione_oggetto);
        final Button rifiuto = dialog.findViewById(R.id.annulla_cancellazione_oggetto);

        dialog.show();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataBaseHelper.deleteOggetto(idOggetto)){
                    dialog.dismiss();
                    finish();
                }
            }
        });

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }

}
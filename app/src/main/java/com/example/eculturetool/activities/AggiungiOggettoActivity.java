package com.example.eculturetool.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.fragments.DialogAddOggettoFragment;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AggiungiOggettoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String OBJECTS_IMAGES_DIR = "objects_images";
    public static final String PLACEHOLDER_OGGETTO = "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fpottery.png?alt=media&token=4551fc3f-2d22-4e91-8b09-baab87588d45";
    private DataBaseHelper dataBaseHelper;          //Riferimento al database
    private Zona zona;                              //zona che verrà selezionata nello spinner
    private EditText nomeOggetto, descrizioneOggetto;
    private Spinner tipologiaOggetto;
    private Button creaOggetto;
    private ProgressBar progressBarAddOggetto;
    private ProgressBar progressBarImg;
    private ImageView imgOggetto;
    private ActivityResultLauncher<Intent> startForObjectImageUpload;
    private Uri imgUri;
    private Permissions permissions;
    private TipologiaOggetto tipologia;
    private FloatingActionButton changeImg;
    private View parentLayout;

    //Si recupera questa lista per fare in modo che l'utente non crei un oggetto con lo stesso nome di uno precedente
    List<Oggetto> oggettiList = new ArrayList<>();

    //VARIABILI GESTIONE SPINNER PER LE ZONE
    private Spinner spinnerZone;
    private List<String> nomiZoneList = new ArrayList<>();
    private List<Zona> zoneList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_oggetti);
        permissions = new Permissions();
        imgOggetto = findViewById(R.id.imgOggetto);
        nomeOggetto = findViewById(R.id.nome_oggetto_add);
        descrizioneOggetto = findViewById(R.id.descrizione_oggetto_add);
        tipologiaOggetto = findViewById(R.id.spinner_tipologia_oggetto_add);
        creaOggetto = findViewById(R.id.creaOggetto);
        progressBarAddOggetto = findViewById(R.id.progressAddOggetto);
        progressBarImg = findViewById(R.id.progressImg);
        changeImg = findViewById(R.id.change_imgUser);
        spinnerZone = findViewById(R.id.spinner_zona_add);
        dataBaseHelper = new DataBaseHelper(this);
        parentLayout = findViewById(android.R.id.content);
        imgOggetto.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.pottery));

        startForObjectImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    progressBarImg.setVisibility(View.VISIBLE);
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        imgUri = uri;
                        Glide.with(this).load(imgUri).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBarImg.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBarImg.setVisibility(View.GONE);
                                return false;
                            }
                        }).circleCrop().into(imgOggetto);
                    }
                });

        changeImg.setOnClickListener(onClickListener -> {
            if(permissions.checkConnection(getApplicationContext())){

                Intent uploadImageIntent = new Intent(this, UploadImageActivity.class);
                uploadImageIntent.putExtra("directory", OBJECTS_IMAGES_DIR);
                startForObjectImageUpload.launch(uploadImageIntent);
            }else{
            Snackbar snackBar = permissions.getPermanentSnackBarWithOkAction(parentLayout, "È necessaria una connessione ad internet per avere accesso a questa funzione");
            snackBar.show();
            }
        });


        oggettiList = getListOggettiCreati();
        setZoneSpinner();
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Crea un ArrayAdapter usando un array di stringhe e uno spinner predefinito
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipologie_oggetti, android.R.layout.simple_spinner_item);
        // Specificare il layout da utilizzare quando viene visualizzato l'elenco delle scelte
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applico l'adapter allo spinner
        tipologiaOggetto.setAdapter(adapter);
        tipologiaOggetto.setOnItemSelectedListener(this);
        creaOggetto.setOnClickListener(view -> {
            creazioneOggetto();
            finish();
        });
    }


    /**
     * Metodo per settare l'elenco delle zone nello spinner relativo. Per ora ci sono solo valori statici
     */
    private void setZoneSpinner() {
        zoneList.clear();       //pulisce la lista prima di riempirla
        nomiZoneList.clear();   //pulisce la lista prima di riempirla

        zoneList = dataBaseHelper.getZone();

        if(zoneList == null || zoneList.size() == 0){
            openDialog();
        }else {
            //Avvalora nomiZoneList con i nomi di ogni singola zona
            for (int i = 0; i < zoneList.size(); i++) {
                nomiZoneList.add(zoneList.get(i).getNome());
            }

            ArrayAdapter<String> nomiZoneListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, nomiZoneList);
            spinnerZone.setAdapter(nomiZoneListAdapter);

            spinnerZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    zona = zoneList.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }


    private void openDialog() {
        DialogAddOggettoFragment dialogAddOggettoFragment = new DialogAddOggettoFragment();
        dialogAddOggettoFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void creazioneOggetto() {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //La progressbar diventa visibile
                progressBarAddOggetto.setVisibility(View.VISIBLE);
            }
        });

        String nome = nomeOggetto.getText().toString().trim();
        String descrizione = descrizioneOggetto.getText().toString().trim();
        int idOggettoCreato;

        if (nome.isEmpty()) {
            nomeOggetto.setError(getResources().getString(R.string.nome_oggetto_richiesto));
            nomeOggetto.requestFocus();
            progressBarAddOggetto.setVisibility(View.INVISIBLE);
            return;
        }

        if (controlloEsistenzaNomeOggetto(nome)) {
            nomeOggetto.requestFocus();
            nomeOggetto.setError(getResources().getString(R.string.nome_esistente));
            progressBarAddOggetto.setVisibility(View.INVISIBLE);
            return;
        }

        if (descrizione.isEmpty()) {
            descrizioneOggetto.setError(getResources().getString(R.string.descrizione_richiesta));
            descrizioneOggetto.requestFocus();
            progressBarAddOggetto.setVisibility(View.INVISIBLE);
            return;
        }

        if (tipologiaOggetto == null) {
            tipologiaOggetto.requestFocus();
            progressBarAddOggetto.setVisibility(View.INVISIBLE);
            return;
        }
        if(permissions.checkConnection(getApplicationContext())){
            if(imgUri == null){
                Toast.makeText(this, getResources().getString(R.string.inserimento_immagine), Toast.LENGTH_LONG).show();
                progressBarAddOggetto.setVisibility(View.INVISIBLE);
                return;
            }
        }else{
            imgUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fpottery.png?alt=media&token=4551fc3f-2d22-4e91-8b09-baab87588d45");
        }

        Oggetto oggetto = new Oggetto(nome, descrizione, imgUri.toString(), tipologia, zona.getId());


        //aggiungo l'oggetto al database
        if((dataBaseHelper.addOggetto(oggetto)) == -1){
            Toast.makeText(this, getResources().getString(R.string.db_errore_scrittura), Toast.LENGTH_LONG).show();
        }
    }


    private boolean controlloEsistenzaNomeOggetto(String nomeOggetto) {
        boolean isEsistente = false;

        for (int i = 0; i < oggettiList.size(); i++) {
            if (nomeOggetto.compareToIgnoreCase(oggettiList.get(i).getNome()) == 0) {
                isEsistente = true;
            }
        }
        return isEsistente;
    }


    private List<Oggetto> getListOggettiCreati() {
        List<Oggetto> returnList;
        returnList = dataBaseHelper.getOggetti();
        return returnList;
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
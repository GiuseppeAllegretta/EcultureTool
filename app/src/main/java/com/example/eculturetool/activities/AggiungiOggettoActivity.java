package com.example.eculturetool.activities;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.fragments.DialogAddOggettoFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

    private static final String STORREF = "gs://auth-96a19.appspot.com/";
    public static final String OBJECTS_IMAGES_DIR = "objects_images";
    private DataBaseHelper dataBaseHelper;          //Riferimento al database
    private Zona zona;                              //zona che verrà selezionata nello spinner
    private StorageReference mStorageReference;
    private EditText nomeOggetto, descrizioneOggetto;
    private Spinner tipologiaOggetto;
    private Button creaOggetto;
    private ProgressBar progressBar;
    private FloatingActionButton changeImg;
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
        changeImg = findViewById(R.id.change_imgUser);
        spinnerZone = findViewById(R.id.spinner_zona_add);
        dataBaseHelper = new DataBaseHelper(this);


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


        oggettiList = getListOggettiCreati();
        setZoneSpinner();
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

            nomiZoneListAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, nomiZoneList);
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

    /**
     * Metodo che consente di caricare su Firestore un oggetto di tipo bitmap in formato jpeg
     *
     * @param bitmap
     */
    public void uploadFile(int idOggetto, Bitmap bitmap) {
        mStorageReference = FirebaseStorage.getInstance(STORREF).getReference("uploads/qrCode");
        StorageReference fileReferences = mStorageReference.child(System.currentTimeMillis() + "." + "jpeg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileReferences.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Ottiene l'uri e lo salva su SQLite
                fileReferences.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Salva su SQLite la stringa dell'url
                        dataBaseHelper.setQRCode(idOggetto, uri.toString());
                    }
                });
            }
        });


    }

    private void creazioneOggetto() {
        String nome = nomeOggetto.getText().toString().trim();
        String descrizione = descrizioneOggetto.getText().toString().trim();
        int idOggettoCreato;

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
        Oggetto oggetto = new Oggetto(nome, descrizione, imgUri.toString(), tipologia, zona.getId());


        //aggiundo l'oggetto al database
        if(dataBaseHelper.addOggetto(oggetto)){
            List<Oggetto> oggettiUpdate = dataBaseHelper.getOggetti(); //lista di oggetti aggiornata con l'ultimo oggetto creato
            for(Oggetto oggetto1: oggettiUpdate){
                if(oggetto1.getNome().compareTo(nome) == 0){
                    idOggettoCreato = oggetto1.getId();
                    Bitmap bitmap = qrCodeGenerator(idOggettoCreato);
                    uploadFile(idOggettoCreato, bitmap);
                    break;
                }
            }


        }

        //La progressbar diventa visibile
        progressBar.setVisibility(View.INVISIBLE);

        finish();
    }


    /**
     * Metodo che si occupa di generare il QRcode dell'oggetto appena creato
     * @param idOggetto identificativo dell'oggetto appena creato
     * @return bitmap: ovvero l'oggetto di tipo Bitmap che rappresenta il QRcode
     */
    private Bitmap qrCodeGenerator(int idOggetto) {
        Bitmap bitmap = null;

        //inizializzazione multi format writer
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            //inizializzazione bit matrix
            BitMatrix matrix = writer.encode(String.valueOf(idOggetto), BarcodeFormat.QR_CODE, 350, 350);

            //inizializzazione barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();

            //inizializzazione bitmap
            bitmap = encoder.createBitmap(matrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
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
package com.example.eculturetool.activities;

import static com.example.eculturetool.activities.AggiungiOggettoActivity.PLACEHOLDER_OGGETTO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
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
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DettaglioOggettoActivity extends AppCompatActivity {

    public static final String OBJECTS_IMAGES_DIR = "objects_images";
    private static final String STORREF = "gs://auth-96a19.appspot.com/";
    private DataBaseHelper dataBaseHelper;          //riferimento del database
    private Oggetto oggetto;                        //oggetto di cui si vedono i dati a schermo
    private List<Zona> zoneList;                    //L'insieme di tutte le zone
    private int idOggetto, luogoCorrente;   //identificativi dell'oggetto e luogo
    private String qrCode;
    private String emailOspite = "admin@gmail.com"; //email dell'account ospite
    private Permissions permissions;
    private ActivityResultLauncher<Intent> startForObjectImageUpload;
    private TextView nomeOggetto, descrizioneOggetto, tipologiaOggetto, zonaAppartenenza;
    private ImageView immagineOggetto;
    private FloatingActionButton cambiaImmagine, modificaOggetto;
    private Toolbar myToolbar;
    private Button eliminaOggetto;
    private Button qrCodeBtn;
    private Context context;
    private Uri imgUri;
    private ProgressBar progressBar;
    private ProgressBar progressBarQr;
    private View parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_oggetto);
        permissions = new Permissions();
        myToolbar = (Toolbar) findViewById(R.id.toolbarOggetto);
        parentLayout = findViewById(android.R.id.content);
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
        progressBarQr = findViewById(R.id.progressBarQr);
        qrCodeBtn = findViewById(R.id.qrCode);

        //Metodo di scroll per la textView
        descrizioneOggetto.setMovementMethod(new ScrollingMovementMethod());


        //Recupero dei dati dall'intent
        Intent intent = getIntent();
        idOggetto = intent.getIntExtra(Oggetto.Keys.ID, 0);     //id dell'oggetto selezionato dalla recyclerView
        luogoCorrente = intent.getIntExtra(Luogo.Keys.ID, 0);
        zoneList = (List<Zona>) intent.getSerializableExtra("ZONELIST");


        startForObjectImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        imgUri = uri;
                        dataBaseHelper.setImageOggetto(idOggetto, imgUri.toString());

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
                });

        //Metodo che consente di scondere le view in caso di accesso con l'account ospite
        nascondiView();
    }

    /**
     * Questo metodo consente di nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite
     */
    private void nascondiView() {
        dataBaseHelper = new DataBaseHelper(this);

        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        if (emailCuratore.compareTo(emailOspite) == 0) {
            cambiaImmagine.setVisibility(View.INVISIBLE);
            eliminaOggetto.setVisibility(View.INVISIBLE);
            modificaOggetto.setVisibility(View.INVISIBLE);
            qrCodeBtn.setVisibility(View.INVISIBLE);
        }
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
                intent.putExtra("ZoneList", (Serializable) zoneList);
                startActivity(intent);
            }
        });

        cambiaImmagine.setOnClickListener(onClickListener -> {
            if (permissions.checkConnection(getApplicationContext())) {
                Intent uploadImageIntent = new Intent(this, UploadImageActivity.class);
                uploadImageIntent.putExtra("directory", OBJECTS_IMAGES_DIR);
                startForObjectImageUpload.launch(uploadImageIntent);
            }else{
                Snackbar snackBar = permissions.getPermanentSnackBarWithOkAction(parentLayout, getResources().getString(R.string.connessione_internet));
                snackBar.show();
            }
        });


        qrCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        //Messaggio che rende invisibile la progressBar
                        if (message.what == 1) {
                            progressBarQr.setVisibility(View.INVISIBLE);
                        }
                        return true;
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //La progressbar diventa visibile
                        progressBarQr.setVisibility(View.VISIBLE);
                    }
                });
                if(permissions.checkConnection(getApplicationContext())) {
                    //Check qr code esistente
                    if (qrCode == null) {
                        Bitmap bitmap = qrCodeGenerator(idOggetto);
                        uploadFile(idOggetto, bitmap);
                        oggetto = dataBaseHelper.getOggettoById(idOggetto);
                        displayPopupImage(oggetto.getUrlQrcode());
                    }else{
                        displayPopupImage(qrCode);
                    }
                } else{
                    //Rendo la progressBar gestita dall'handler non visibile attraverso un messaggio
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    Snackbar snackBar = permissions.getPermanentSnackBarWithOkAction(parentLayout, getResources().getString(R.string.connessione_internet));
                    snackBar.show();
                }
            }
        });

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

    /**
     * Metodo che consente di caricare su Firestore un oggetto di tipo bitmap in formato jpeg
     *
     */
    public void uploadFile(int idOggetto, Bitmap bitmap) {
        StorageReference mStorageReference = FirebaseStorage.getInstance(STORREF).getReference("uploads/qrCode");
        StorageReference fileReferences = mStorageReference.child(System.currentTimeMillis() + "." + "jpeg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //Ottiene l'uri e lo salva su SQLite
        //Salva su SQLite la stringa dell'url
        StorageTask uploadTask = fileReferences.putBytes(data)
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(taskSnapshot -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    //Ottiene l'uri e lo salva su SQLite
                    fileReferences.getDownloadUrl().addOnSuccessListener(uri -> {
                        qrCode = uri.toString();
                        //Salva su SQLite la stringa dell'url
                        dataBaseHelper.setQRCode(idOggetto, qrCode);
                        displayPopupImage(qrCode);
                    });
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void displayPopupImage(String uri) {
        //Glide.with(dialog.getContext()).load(uri).into(image);
        Glide.with(this)
                .asBitmap()
                .load(uri)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Handler handler = new Handler(getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //La progressbar diventa visibile
                                progressBarQr.setVisibility(View.INVISIBLE);
                            }
                        });
                        final Dialog dialog = new Dialog(DettaglioOggettoActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.qrcode_dialog);

                        final ImageView image = dialog.findViewById(R.id.qrCodeImg);
                        image.setImageBitmap(resource);
                        dialog.show();
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

    private void setDatiOggetto() {
        dataBaseHelper = new DataBaseHelper(this);
        oggetto = dataBaseHelper.getOggettoById(idOggetto);

        if (oggetto != null) {
            String uriImg = oggetto.getUrl();
            if (!Objects.equals(uriImg, PLACEHOLDER_OGGETTO)) {
                if(permissions.checkConnection(getApplicationContext())){
                    Glide.with(context).load(uriImg).circleCrop().into(immagineOggetto);
                }else{
                    Glide.with(context).load(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.image_not_found)).circleCrop().into(immagineOggetto);
                }
            } else {
                Glide.with(context).load(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.pottery)).circleCrop().into(immagineOggetto);
            }
            getSupportActionBar().setTitle(oggetto.getNome());
            nomeOggetto.setText(oggetto.getNome());
            descrizioneOggetto.setText(oggetto.getDescrizione());
            tipologiaOggetto.setText(setTipologia(oggetto.getTipologiaOggetto()));
            zonaAppartenenza.setText(getNomeZona(oggetto.getZonaAppartenenza()));
            qrCode = oggetto.getUrlQrcode();
        }
    }

    /**
     * Metodo che recupera il nome di una zona in base all'id della zona fornito in input
     * @param id id della zona in cui è contenuto l'oggetto
     * @return nomeZona cioè il nome di una zona sotto forma di stringa
     */
    private String getNomeZona(int id) {
        String nomeZona = null;

        for (Zona zona : zoneList) {
            if (zona.getId() == id) {
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
        dialog.setContentView(R.layout.dialog_layout);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);

        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText(getResources().getString(R.string.cancella_oggetto));

        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBaseHelper.deleteOggetto(idOggetto)) {
                    dialog.dismiss();
                    finish();
                }
            }
        });

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }

}
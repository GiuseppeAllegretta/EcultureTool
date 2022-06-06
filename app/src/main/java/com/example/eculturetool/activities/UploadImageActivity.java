package com.example.eculturetool.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity che si occupa dell'upload di un'immagine avvalendosi del servizio Firebase Storage.
 * É possibile effettuare l'upload di un'immagine per cambiare l'immagine del profilo e durante la creazione di un nuovo oggetto.
 * L'immagine stessa può essere catturata dalla fotocamera o può essere scelta appositamente dalla galleria.
 */
public class UploadImageActivity extends AppCompatActivity {

    /**
     * Url Firebase Storage
     */
    public static final String STORAGE_REF = "gs://auth-96a19.appspot.com";

    /**
     * Riferimento Firebase Storage
     */
    private StorageReference storageReference;

    /**
     * Istanza di Permissions
     */
    private Permissions permission = new Permissions();

    /**
     * Riferimento alla ImageView contenente l'immagine da caricare
     */
    private ImageView image;

    /**
     * Riferimento al placeholder dell'immagine
     */
    private ImageView imagePlaceHolder;

    /**
     * Riferimento alla progress bar
     */
    private ProgressBar progressBar;

    /**
     * L'uri dell'immagine caricata
     */
    private Uri uri;

    /**
     * Il file corrispondente alla foto scattata
     */
    private File photoFile;

    /**
     * Riferimento al parent layout
     */
    private View parentLayout;

    /**
     * Launcher che gestisce la scelta di un'immagine dalla galleria
     */
    private ActivityResultLauncher<Intent> chooseFileResultLaunch;

    /**
     * Launcher che gestisce l'acquisizione di una foto dalla fotocamera
     */
    private ActivityResultLauncher<Intent> tookPhotoResultLaunch;

    /**
     * Il task che controlla l'aupload dell'immagine
     */
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        parentLayout = findViewById(android.R.id.content);
        Button btnScatta = findViewById(R.id.btnScatta);
        Button btnSeleziona = findViewById(R.id.btnSeleziona);
        Button btnSalva = findViewById(R.id.btnSalva);
        image = findViewById(R.id.imageView);
        imagePlaceHolder = findViewById(R.id.imagePlaceHolder);
        progressBar = findViewById(R.id.progressBar);

        //Setting degli elementi dell'interfaccia
        getSupportActionBar().setTitle(getString(R.string.carica_immagine));

        //permette di tornare indietro
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Determino il riferimento al nodo che rappresenta la cartella in cui verrà caricata l'immagine su Firebase Storage
        storageReference = FirebaseStorage.getInstance(STORAGE_REF).getReference().child("uploads").child(getIntent().getStringExtra("directory"));

        //Launcher che gestisce la scelta di un'immagine dalla galleria
        chooseFileResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                uri = result.getData().getData();
                                imagePlaceHolder.setImageResource(android.R.color.transparent);
                                //Scarico l'immagine utilizzando l'uri e la imposto nella ImageView
                                Glide.with(UploadImageActivity.this).load(uri).placeholder(R.drawable.ic_profile).into(image);
                            }
                        }
                    }
                });

        //Launcher che gestisce l'acquisizione di una foto dalla fotocamera
        tookPhotoResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        uri = Uri.fromFile(photoFile);
                        if (result.getResultCode() == RESULT_OK) {
                            imagePlaceHolder.setImageResource(android.R.color.transparent);
                            //Scarico l'immagine utilizzando l'url e la imposto nella ImageView
                            Glide.with(UploadImageActivity.this).load(uri).placeholder(R.drawable.ic_profile).into(image);
                        }
                    }
                });

        //Listner del bottone per selezionare un'immagine dalla galleria
        btnSeleziona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Controllo dei permessi necessari
                if (!permission.checkStoragePermission(UploadImageActivity.this, parentLayout)) {
                    permission.requestStoragePermission(UploadImageActivity.this, parentLayout);
                } else {
                    //Intent per la scelta del file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    chooseFileResultLaunch.launch(intent);
                }
            }
        });

        //Listner del bottone per scattare una foto utilizzando la fotocamera di sistema
        btnScatta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Controllo dei permessi necessari
                if (!permission.checkCameraPermission(UploadImageActivity.this, parentLayout)) {
                    permission.requestCameraPermission(UploadImageActivity.this, parentLayout);
                } else {
                    apriFotocamera();
                }
            }
        });

        //Listner del bottone per eseguire l'upload di un'immagine/foto
        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Controllo che il task legato all'upload sia effettivamente terminato
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(UploadImageActivity.this, getString(R.string.caricamento_in_corso), Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    /**
     * Metodo per la creazione di un file locale con estensione .jpg e avente come filename il timestamp
     * relativo alla sua creazione
     * @return un file con estensione .jpg
     * @throws IOException
     */
    private File creaFileImmagine() throws IOException {
        //Imposto come filename il timestamp
        String imageFileName = SimpleDateFormat.getDateTimeInstance().format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Creo e ritorno un file con estensione .jpg
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    /**
     * Metodo che gestisce l'apertura della fotocamera per l'acquisizione di una foto.
     * Si assicura che il dispositivo abbia a disposizione una fotocamera per farlo.
     */
    private void apriFotocamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            //Mi assicuro che ci sia un'activity della fotocamera per gestire l'intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Creazione del file immagine dove salvare la foto
                photoFile = null;
                try {
                    photoFile = creaFileImmagine();
                } catch (IOException ex) {
                    //Si è verificato un problema nella creazione del file immagine
                    Toast.makeText(UploadImageActivity.this, getString(R.string.errore), Toast.LENGTH_SHORT).show();
                }
                // Continua se il file è stato correttamente creato
                if (photoFile != null) {
                    //Ottengo l'uri del file appena creato
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    //Avvio della fotocamera di sistema per l'acquisizione della foto
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    tookPhotoResultLaunch.launch(takePictureIntent);
                }
            }
        } catch (ActivityNotFoundException e) {
            //Toast per segnalare la mancanza di una fotocamera sul dispositivo
            Toast.makeText(UploadImageActivity.this, getString(R.string.fotocamera_non_disponibile), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo per determinare l'estensione di un file selezionato
     * @param uri uri del file
     * @return stringa dell'estensione del file
     */
    private String getEstensioneFile(Uri uri) {
        String extension;
        //Controllo lo schema dell'uri
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(this.getContentResolver().getType(uri));
        } else {
            //evita il ritorno di valori null a causa di caratteri speciali
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

    /**
     * Metodo che si occupa della gestione del task a cui viene delegato l'upload dell'immagine.
     * L'andamento del caricamento è visibile attraverso la progress bar determinata; al suo termine
     *  viene scaricata l'immagine utilizzando l'uri ricavato e viene impostata nella ImageView
     */
    private void uploadFile() {
        //controllo che l'uri non sia null
        if (uri != null) {
            //Ottengo un riferimento al nodo in cui verrà effettuato l'upload
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getEstensioneFile(uri));
            //Task che si occupa dell'upload
            uploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Progress bar settata a 0
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), getString(R.string.upload_effettuato), Toast.LENGTH_LONG).show();
                            //Ottengo l'uri dell'immagine caricata su Firebase Storage
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Imposto l'uri come risultato dell'intent
                                    Intent intent = new Intent();
                                    intent.setData(uri);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast per segnalare un errore riscontrato nell'upload
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            //Progress bar aggiornata per segnalare l'andamento dell'upload
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            //Toast per segnalare che nessun file è stato segnalato per l'upload
            Toast.makeText(getApplicationContext(), getString(R.string.nessun_file_selezionato), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions perm = new Permissions();
        switch (requestCode){
            //Permesso scrittura storage esterno
            case Permissions.STORAGE_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Permesso rifiutato
                        if (!(grantResult == PackageManager.PERMISSION_GRANTED)) {
                            //Mi assicuro la prima volta che l'utente voglia effettivamente rifiutare il permesso
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                //Messaggio per la richiesta di riconferma
                                perm.showMessageOkCancel(Permissions.STORAGE_PERMISSION_MSG,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        perm.requestStoragePermission(UploadImageActivity.this, parentLayout);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, Permissions.STORAGE_PERMISSION_MSG);
                                                        snackBar.show();
                                                        break;
                                                }
                                            }
                                        }, this);
                            } else {
                                //L'utente ha confermato la volontà di negare il permesso e viene mostrato un avviso
                                Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, getString(R.string.accesso_risorse_esterne));
                                snackBar.show();
                            }
                        }
                    }
                }
                break;
            //Permesso fotocamera dispositivo
            case Permissions.CAMERA_REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.CAMERA)) {
                        //Permesso rifiutato
                        if (!(grantResult == PackageManager.PERMISSION_GRANTED)) {
                            //Mi assicuro la prima volta che l'utente voglia effettivamente rifiutare il permesso
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                                //Messaggio per la richiesta di riconferma
                                perm.showMessageOkCancel(Permissions.CAMERA_PERMISSION_MSG,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        perm.requestCameraPermission(UploadImageActivity.this, parentLayout);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, Permissions.CAMERA_PERMISSION_MSG);
                                                        snackBar.show();
                                                        break;
                                                }
                                            }
                                        }, this);
                            } else {
                                //L'utente ha confermato la volontà di negare il permesso e viene mostrato un avviso
                                Snackbar snackBar = perm.getPermanentSnackBarWithOkAction(parentLayout, getString(R.string.accesso_fotocamera));
                                snackBar.show();
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gestione della freccia per tornare indietro
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
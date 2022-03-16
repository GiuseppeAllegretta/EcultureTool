package com.example.eculturetool.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Oggetto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

public class CreateObjectActivity extends AppCompatActivity {

    private ImageView image;
    private ProgressBar progressBar;
    private EditText nome;
    private EditText tipologia;
    private EditText descrizione;

    private final String DATAREF = "https://auth-96a19-default-rtdb.europe-west1.firebasedatabase.app/"; //Link al realtime database Firebase
    private final String STORREF = "gs://auth-96a19.appspot.com/"; //Link allo storage Firebase

    DatabaseReference root = FirebaseDatabase.getInstance(DATAREF).getReference("uploads").child("objects_images"); //Acquisizione percorso salvataggio oggetti
    StorageReference storageReference = FirebaseStorage.getInstance(STORREF).getReference("uploads").child("objects_images"); //Acqusizione percorso storage

    private Uri uri; //L'URI dell'immagine caricata dall'utente

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_object);

        // Set variabili interfaccia
        image = findViewById(R.id.image);
        Button btnCrea = findViewById(R.id.btnCrea);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        nome = findViewById(R.id.nome); // Nome dell'opera
        tipologia = findViewById(R.id.tipologia); // Tipologia dell'opera
        descrizione = findViewById(R.id.descrizione); // Descrizione dell'opera

        // Check dei permessi; se sono false si richiede di accettare i permessi
        image.setOnClickListener(onClickListener -> {
            boolean permissions = true;
            if (!checkCameraPermission()) {
                permissions = false;
                requestCameraPermission();
            }
            if (!checkStoragePermission()) {
                permissions = false;
                requestStoragePermission();
            }
            if (permissions)
                PickImage();
        });

        // Procedura di upload su Firebase se l'URI non è nullo
        btnCrea.setOnClickListener(onClickListener -> {
            if (uri != null) {
                uploadImageToFirebase(uri);
            } else {
                Toast.makeText(getApplicationContext(), "Nessuna immagine selezionata!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo che consente di caricare una risorsa URI su Firebase
    private void uploadImageToFirebase(Uri uri) {
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnCompleteListener(task -> fileRef.getDownloadUrl().addOnSuccessListener(onSuccessListener -> {
            String oggettoId = root.push().getKey();
            Oggetto oggetto = new Oggetto(oggettoId, nome.getText().toString(), descrizione.getText().toString(),
                    onSuccessListener.toString());
            root.child(oggettoId).setValue(oggetto);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(CreateObjectActivity.this, "Upload completato!", Toast.LENGTH_SHORT).show();
        }))
                .addOnProgressListener(onProgessListener -> progressBar.setVisibility(View.VISIBLE))
                .addOnFailureListener(onFailureListener -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(CreateObjectActivity.this, "Upload fallito!", Toast.LENGTH_SHORT).show();
                });
    }

    // Permette di acquisire un'immagine da diverse fonti, anche da fotocamera, e di eseguirne il crop
    private void PickImage() {
        CropImage.activity().start(this);
    }

    // Ritorna l'estensione di un file
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }

    // Se l'acquisizione dell'immagine avviene correttamente, la nuova immagine viene impostata come immagine attuale
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK && data != null) {
                image.setBackgroundResource(android.R.color.transparent);
                uri = result.getUri();
                try {
                    image.setImageURI(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

}
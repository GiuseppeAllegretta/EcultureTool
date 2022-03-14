package com.example.eculturetool.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.eculturetool.R;
import com.example.eculturetool.Upload;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.provaoggetti.EntityOggetto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;

public class FilePicker extends AppCompatActivity {
    private Connection connection = new Connection();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private ImageView image;
    private Button btn;
    private Uri uri;

    private EditText nome;
    private EditText tipologia;
    private EditText descrizione;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_picker);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("oggetti");
        mStorageRef = FirebaseStorage.getInstance().getReference("oggetti");

        image = findViewById(R.id.image);
        image.setImageBitmap(null);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pick = true;
                if (pick) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else
                        PickImage();
                } else {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else
                        PickImage();
                }
            }
        });


        btn = findViewById(R.id.btnCrea);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = findViewById(R.id.nome);
                tipologia = findViewById(R.id.tipologia);
                descrizione = findViewById(R.id.descrizione);

                EntityOggetto oggetto = new EntityOggetto(13, nome.getText().toString(), tipologia.getText().toString(),
                        descrizione.getText().toString(), "urlll");
                //mFirebaseDatabase.push().setValue(oggetto);
                DatabaseReference oggettoReference = mFirebaseDatabase.push();
                oggettoReference.setValue(oggetto);
                uploadFile(nome.getText().toString(), oggettoReference);

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(FilePicker.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(nome.getText().toString(), mFirebaseDatabase);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image.setBackgroundResource(android.R.color.transparent);
                uri = result.getUri();
                try {
                    InputStream stream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    image.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void PickImage() {
        CropImage.activity().start(this);
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(String nomeFile, DatabaseReference oggettoReference) {
        if (uri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));

            mUploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(), "Upload effettuato con successo", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(nomeFile, taskSnapshot.getTask().toString());
                            String uploadId = mFirebaseDatabase.push().getKey();
                            mFirebaseDatabase.child(uploadId).setValue(upload);
                            //myRef = connection.getMyRefCuratore();
                            //myRef.child("img").setValue(fileReference);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Riferimento a realtime database
                                    mFirebaseInstance = FirebaseDatabase.getInstance();
                                    // get reference to 'curatori' node
                                    mFirebaseDatabase = mFirebaseInstance.getReference("oggetti");
                                    //aggiorno l'url dell'immagine
                                    mFirebaseDatabase.child(oggettoReference.toString()).child("url").setValue(uri.toString());
                                    //Intent activity3Intent = new Intent(UploadImageActivity.this, ProfileFragment.class);
                                    //activity3Intent.putExtra("img", uri);
                                    //setResult(1888,activity3Intent);
                                    finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Nessun file selezionato!", Toast.LENGTH_SHORT).show();
        }
    }

}
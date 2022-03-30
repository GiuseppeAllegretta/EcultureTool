package com.example.eculturetool.activities;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.Upload;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadImageActivity extends AppCompatActivity {

    private final Connection connection = new Connection();
    private ImageView mImageView, imagePlaceHolder;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private File photoFile;
    private View parentLayout;
    ActivityResultLauncher<Intent> chooseFileResultLaunch;
    ActivityResultLauncher<Intent> tookPhotoResultLaunch;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        parentLayout = findViewById(android.R.id.content);
        Button btnScatta = findViewById(R.id.btnScatta);
        Button btnSeleziona = findViewById(R.id.btnSeleziona);
        Button btnSalva = findViewById(R.id.btnSalva);
        mImageView = findViewById(R.id.imageView);
        imagePlaceHolder = findViewById(R.id.imagePlaceHolder);

        mProgressBar = findViewById(R.id.progressBar);

        mStorageRef = connection.getStorage().getReference("uploads").child(getIntent().getStringExtra("directory"));
        mDatabaseRef = connection.getDatabase().getReference("uploads").child(getIntent().getStringExtra("directory"));

        chooseFileResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == UploadImageActivity.RESULT_OK) {
                            mImageUri = result.getData().getData();
                            Glide.with(UploadImageActivity.this).load(mImageUri).placeholder(R.drawable.ic_profile).into(mImageView);
                        }
                    }
                });

        tookPhotoResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        mImageUri = Uri.fromFile(photoFile);
                        Glide.with(UploadImageActivity.this).load(mImageUri).placeholder(R.drawable.ic_profile).into(mImageView);
                        imagePlaceHolder.setImageResource(android.R.color.transparent);
                    }
                });

        Permissions permissions = Permissions.getInstance();
        btnSeleziona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissions.checkStoragePermission(UploadImageActivity.this, parentLayout)) {
                    permissions.requestStoragePermission(UploadImageActivity.this);
                } else {
                    openFileChooser();
                }
            }
        });

        btnScatta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissions.checkCameraPermission(UploadImageActivity.this, parentLayout)) {
                    permissions.requestCameraPermission(UploadImageActivity.this);
                } else {
                    openCamera();
                }
            }
        });

        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadImageActivity.this, "Caricamento in corso...", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        chooseFileResultLaunch.launch(intent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File...
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    tookPhotoResultLaunch.launch(takePictureIntent);
                }
            }
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private String getFileExtension(Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(this.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), "Upload effettuato con successo", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(taskSnapshot.getTask().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
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
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Nessun file selezionato!", Toast.LENGTH_SHORT).show();
        }
    }

}
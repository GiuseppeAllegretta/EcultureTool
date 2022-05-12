package com.example.eculturetool;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.eculturetool.database.DataBaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Upload {
    private static final String STORREF = "gs://auth-96a19.appspot.com/";

    private String mImageUrl;
    private String mName;
    private Uri mImageUri;
    private StorageReference mStorageReference;
    private DataBaseHelper dataBaseHelper;


    public Upload() {
        //empty constructor needed
    }

    public Upload(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public Upload(Context context, String name) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        this.mName = name;
        mStorageReference = FirebaseStorage.getInstance(STORREF).getReference("uploads/qrCode");
        dataBaseHelper = new DataBaseHelper(context);
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }


    /**
     * Metodo che consente di caricare su Firestore un oggetto di tipo bitmap in formato jpeg
     *
     * @param bitmap
     */
    public void uploadFile(int idOggetto, Bitmap bitmap) {

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
}

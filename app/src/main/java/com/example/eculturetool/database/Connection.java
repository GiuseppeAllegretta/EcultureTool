package com.example.eculturetool.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Connection {

    private static final String DBREF = "https://auth-96a19-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final String STORREF = "gs://auth-96a19.appspot.com/";

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance(DBREF).getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance(STORREF).getReference();

    private static FirebaseAuth auth;
    private static FirebaseStorage storage;
    private static FirebaseDatabase database;
    private static FirebaseUser user;

    public Connection() {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }


    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public DatabaseReference getRefCuratore() {
        return databaseReference.child("curatori").child(user.getUid());
    }

    public DatabaseReference getRefLuogo() {
        return databaseReference.child("luoghi").child(user.getUid());
    }

    public static String getUidCuratore() {
        return user.getUid();
    }

    public final String getDBREF() {
        return DBREF;
    }

    public final String getSTORREF() {
        return STORREF;
    }


}

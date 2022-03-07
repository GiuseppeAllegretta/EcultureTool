package com.example.eculturetool.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Connection {

    private final String REF = "https://auth-96a19-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseUser user;

    public Connection(){
        database = FirebaseDatabase.getInstance(REF);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public DatabaseReference getMyRefCuratore(){
        return database.getReference("curatori").child(user.getUid());
    }
}

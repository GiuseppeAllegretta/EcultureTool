package com.example.eculturetool.provaoggetti;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.utilities.CircleTransform;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ElencoOggetti extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Connection connection = new Connection();
    private Integer size;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_object);

        myRef = mFirebaseInstance.getReference("oggetti");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                size = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        EntityOggetto entityOggetto = new EntityOggetto( 6, "Nome", "Descrizione", "tipologia", "https://www.zeusnews.it/img/4/8/1/6/2/0/026184-620-google-vedi-immagini.jpg");

        //Riferimento a realtime database
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'curatori' node
        mFirebaseDatabase = mFirebaseInstance.getReference("oggetti").child(entityOggetto.getId().toString());
        //aggiorno l'url dell'immagine
        //mFirebaseDatabase.child(connection.getUser().getUid()).child("img").setValue(uri.toString());

        mFirebaseDatabase.setValue(entityOggetto);



    }

}

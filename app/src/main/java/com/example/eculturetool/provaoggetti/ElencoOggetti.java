package com.example.eculturetool.provaoggetti;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Oggetto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ElencoOggetti extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference oggetti;
    private Connection connection = new Connection();
    List<Oggetto> listaOggetti;
    private Integer size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_object);

        //Riferimento a realtime database
        mFirebaseInstance = FirebaseDatabase.getInstance();
        oggetti = mFirebaseInstance.getReference("oggetti");

        oggetti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaOggetti = new ArrayList<>();
                //Ottengo il numero di oggetti già creati
                size = Math.toIntExact(snapshot.getChildrenCount());
                for (DataSnapshot child : snapshot.getChildren()) {
                    //Aggiungo il nodo alla lista di oggetti
                    listaOggetti.add(child.getValue(Oggetto.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

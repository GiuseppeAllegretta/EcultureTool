package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DettaglioZonaActivity extends AppCompatActivity {
    private Connection connection = new Connection();
    private String idZona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_zona);

        //recupero dati dall'intent

        Intent intent=getIntent();
        idZona=intent.getStringExtra("ZONA");
        System.out.println("ID ZONA-->"+idZona);
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.getRefZone().child(idZona).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //devo creare il layout e poi settare i campi
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ModificaProfiloActivity extends AppCompatActivity {

    private Connection connection = new Connection();
    private DatabaseReference myRef;
    private EditText nome, cognome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        nome = findViewById(R.id.edit_name_profile);
        cognome = findViewById(R.id.edit_cognome_profile);

        myRef = connection.getMyRefCuratore();
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nome.setText(snapshot.getValue(Curatore.class).getNome());
                cognome.setText(snapshot.getValue(Curatore.class).getCognome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
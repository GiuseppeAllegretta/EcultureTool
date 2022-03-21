package com.example.eculturetool.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ModificaProfiloActivity extends AppCompatActivity {

    private Connection connection = new Connection();
    private DatabaseReference myRef;
    private EditText nome, cognome;
    private ImageView back, conferma; //icona back e icona conferma

    private final static String NO_ALPHA_REGEX = "[a-zA-Z\\s]*$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        nome = findViewById(R.id.edit_name_profile);
        cognome = findViewById(R.id.edit_cognome_profile);
        back = findViewById(R.id.freccia_back);
        conferma = findViewById(R.id.icona_conferma);

        myRef = connection.getRefCuratore();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modificaDati())
                    onBackPressed();
                else
                    Toast.makeText(ModificaProfiloActivity.this, "Campo vuoto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean modificaDati() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(connection.getDBREF());
        DatabaseReference myRef = database.getReference("curatori").child(connection.getUser().getUid());
        //TODO controllare venga usato

        if (nome.getText().toString().isEmpty()) {
            nome.setError("Inserisci il nome");
            nome.requestFocus();
            return false;
        }

        if (!Pattern.matches(NO_ALPHA_REGEX, nome.getText().toString())) {
            nome.setError("Inserisci nome valido");
            nome.requestFocus();
            return false;
        }


        if (cognome.getText().toString().isEmpty()) {
            cognome.setError("Inserisci il cognome");
            cognome.requestFocus();
            return false;
        }

        if (!Pattern.matches(NO_ALPHA_REGEX, cognome.getText().toString())) {
            cognome.setError("Inserisci cognome valido");
            cognome.requestFocus();
            return false;
        }


        myRef.child("nome").setValue(nome.getText().toString());
        myRef.child("cognome").setValue(cognome.getText().toString());

        return true;
    }

}
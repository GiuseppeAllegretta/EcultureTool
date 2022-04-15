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
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ModificaProfiloActivity extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    private EditText nome, cognome;
    private ImageView back, conferma; //icona back e icona conferma
    private Curatore curatore;

    private final static String NO_ALPHA_REGEX = "[a-zA-Z\\s]*$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        dataBaseHelper = new DataBaseHelper(this);
        nome = findViewById(R.id.edit_name_profile);
        cognome = findViewById(R.id.edit_cognome_profile);
        back = findViewById(R.id.freccia_back);
        conferma = findViewById(R.id.icona_conferma);
        curatore = dataBaseHelper.getCuratore();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Avvaloro le editText
        nome.setText(curatore.getNome());
        cognome.setText(curatore.getCognome());

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
                    finish();
                else
                    Toast.makeText(ModificaProfiloActivity.this, getResources().getString(R.string.campo_vuoto), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean modificaDati() {

        if (nome.getText().toString().isEmpty()) {
            nome.setError(getString(R.string.inserisci_nome));
            nome.requestFocus();
            return false;
        }

        if (!Pattern.matches(NO_ALPHA_REGEX, nome.getText().toString())) {
            nome.setError(getString(R.string.inserisci_nome_valido));
            nome.requestFocus();
            return false;
        }


        if (cognome.getText().toString().isEmpty()) {
            cognome.setError(getString(R.string.inserisci_cognome));
            cognome.requestFocus();
            return false;
        }

        if (!Pattern.matches(NO_ALPHA_REGEX, cognome.getText().toString())) {
            cognome.setError(getString(R.string.inserisci_cognome_valido));
            cognome.requestFocus();
            return false;
        }

        dataBaseHelper.modificaCuratore(nome.getText().toString(), cognome.getText().toString());

        return true;
    }

}
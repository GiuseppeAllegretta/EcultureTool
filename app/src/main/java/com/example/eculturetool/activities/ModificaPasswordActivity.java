package com.example.eculturetool.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;

public class ModificaPasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmPassword;
    private final Connection connection = new Connection();
    private ProgressBar progressBar;

    boolean flagPassword = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        //Recupero i riferimenti delle view presenti nella schermata
        oldPassword = findViewById(R.id.password_attuale);
        newPassword = findViewById(R.id.nuova_password);
        confirmPassword = findViewById(R.id.conferma_nuova_password);
        ImageView frecciaBack = findViewById(R.id.freccia_back_modifica_password);
        ImageView tastoConferma = findViewById(R.id.icona_conferma_modifica_password);
        progressBar = findViewById(R.id.pb);

        frecciaBack.setOnClickListener(onClickListener -> onBackPressed());

        tastoConferma.setOnClickListener(onClickListener -> {
            if (modificaPassword())
                onBackPressed();
        });
    }

    private String passNuova;

    public boolean modificaPassword() {
        String passAttuale, passNuovaConf;
        passAttuale = oldPassword.getText().toString();
        passNuova = newPassword.getText().toString();
        passNuovaConf = confirmPassword.getText().toString();

        if (passAttuale.isEmpty()) {
            oldPassword.setError(getResources().getString(R.string.password_attuale));
            oldPassword.requestFocus();
            return false;
        }

        if (passNuova.isEmpty()) {
            newPassword.setError(getResources().getString(R.string.nuova_password));
            newPassword.requestFocus();
            return false;
        }

        if (passNuovaConf.isEmpty()) {
            confirmPassword.setError(getResources().getString(R.string.conferma_password));
            confirmPassword.requestFocus();
            return false;
        }

        if (passNuova.compareTo(passNuovaConf) != 0) {
            Toast.makeText(this, getResources().getString(R.string.password_non_coincidenti), Toast.LENGTH_SHORT).show();
            newPassword.setError(getResources().getString(R.string.password_non_coincidenti));
            confirmPassword.setError(getResources().getString(R.string.password_non_coincidenti));
            newPassword.requestFocus();
            return false;
        }

        return passwordVerificata(connection.getUser().getEmail(), passAttuale);

    }

    public boolean passwordVerificata(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        connection.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, onCompleteListener -> {
                    if (onCompleteListener.isSuccessful()) {
                        connection.getUser().updatePassword(passNuova)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ModificaPasswordActivity.this, getResources().getString(R.string.password_aggiornata), Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                        flagPassword = true;

                                    } else {
                                        Toast.makeText(ModificaPasswordActivity.this, getResources().getString(R.string.password_non_aggiornata), Toast.LENGTH_SHORT).show();
                                        flagPassword = false;
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_non_coincide), Toast.LENGTH_SHORT).show();
                        oldPassword.setError(getString(R.string.password_non_corretta));
                        oldPassword.requestFocus();
                        progressBar.setVisibility(View.INVISIBLE);
                        flagPassword = false;
                    }
                });
        return flagPassword;
    }
}
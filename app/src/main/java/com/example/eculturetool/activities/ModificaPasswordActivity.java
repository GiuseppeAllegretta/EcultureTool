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
            oldPassword.setError("Inserisci la password attuale");
            oldPassword.requestFocus();
            return false;
        }

        if (passNuova.isEmpty()) {
            newPassword.setError("Inserisci la nuova password");
            newPassword.requestFocus();
            return false;
        }

        if (passNuovaConf.isEmpty()) {
            confirmPassword.setError("Conferma la password");
            confirmPassword.requestFocus();
            return false;
        }

        if (passNuova.compareTo(passNuovaConf) != 0) {
            Toast.makeText(this, "Le due password non coincidono", Toast.LENGTH_SHORT).show();
            newPassword.setError("Le due password non coincidono");
            confirmPassword.setError("Le due password non coincidono");
            newPassword.requestFocus();
            return false;
        }

        return passwordVerificata(connection.getUser().getEmail(), passAttuale);

    }

    public boolean passwordVerificata(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        connection.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, onCompleteListener -> {
                    System.out.println("prima dell'if: " + flagPassword);
                    if (onCompleteListener.isSuccessful()) {
                        connection.getUser().updatePassword(passNuova)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ModificaPasswordActivity.this, "Password Aggiornata", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                        flagPassword = true;

                                    } else {
                                        Toast.makeText(ModificaPasswordActivity.this, "Password non aggiornata", Toast.LENGTH_SHORT).show();
                                        flagPassword = false;
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "La password attuale non coincide", Toast.LENGTH_SHORT).show();
                        oldPassword.setError("Password non corretta");
                        oldPassword.requestFocus();
                        progressBar.setVisibility(View.INVISIBLE);
                        flagPassword = false;
                    }
                });
        return flagPassword;
    }
}
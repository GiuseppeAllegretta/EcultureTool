package com.example.eculturetool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModificaPasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmPassword;
    private ImageView frecciaBack, tastoConferma;
    private Connection connection = new Connection();
    private DatabaseReference myRef;
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
        frecciaBack = findViewById(R.id.freccia_back_modifica_password);
        tastoConferma = findViewById(R.id.icona_conferma_modifica_password);
        progressBar = findViewById(R.id.pb);

        //Ottenimento riferimento al curatore attualmente connesso
        myRef = connection.getMyRefCuratore();
        frecciaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tastoConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modificaPassword())
                    onBackPressed();
            }
        });
    }
private String passNuova;
    public boolean modificaPassword(){
        FirebaseDatabase database = FirebaseDatabase.getInstance(connection.getREF());
        DatabaseReference myRef = database.getReference("curatori").child(connection.getUser().getUid());

        String passAttuale, passNuovaConf;
        passAttuale = oldPassword.getText().toString();
        passNuova = newPassword.getText().toString();
        passNuovaConf = confirmPassword.getText().toString();

        if(passAttuale.isEmpty()){
            oldPassword.setError("Inserisci la password attuale");
            oldPassword.requestFocus();
            return false;
        }

        if(passNuova.isEmpty()){
            newPassword.setError("Inserisci la nuova password");
            newPassword.requestFocus();
            return false;
        }

        if(passNuovaConf.isEmpty()){
            confirmPassword.setError("Conferma la password");
            confirmPassword.requestFocus();
            return false;
        }

        if(passNuova.compareTo(passNuovaConf) != 0){
            Toast.makeText(this, "Le due password non coincidono", Toast.LENGTH_SHORT).show();
            newPassword.setError("Le due password non coincidono");
            confirmPassword.setError("Le due password non coincidono");
            newPassword.requestFocus();
            return false;
        }

        return passwordVerificata(connection.getUser().getEmail().toString(), passAttuale);

    }

    public boolean passwordVerificata(String email, String password){
        progressBar.setVisibility(View.VISIBLE);

        connection.getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("prima dell'if: " + flagPassword);
                        if (task.isSuccessful())
                        {
                            connection.getUser().updatePassword(passNuova)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ModificaPasswordActivity.this, "Password Aggiornata", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                                flagPassword = true;

                                            } else {
                                                Toast.makeText(ModificaPasswordActivity.this, "Password non aggiornata", Toast.LENGTH_SHORT).show();
                                                flagPassword = false;
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "La password attuale non coincide", Toast.LENGTH_SHORT).show();
                            oldPassword.setError("Password non corretta");
                            oldPassword.requestFocus();
                            progressBar.setVisibility(View.INVISIBLE);
                            flagPassword = false;
                        }
                    }
                });
        return flagPassword;
    }
}
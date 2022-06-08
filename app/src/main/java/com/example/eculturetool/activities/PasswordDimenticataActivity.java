package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private EditText emailEditText;
    private ProgressBar progressBar;
    private Button resetPasswordButton;
    private DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dimenticata);

        //Acquisizione riferimenti view nell'acitivty
        emailEditText = findViewById(R.id.emailReset);
        resetPasswordButton = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBarReset);
        progressBar.setVisibility(View.INVISIBLE);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        resetPasswordButton.setOnClickListener(onClickListener -> resetPassword());

        getSupportActionBar().setTitle(R.string.password_dimenticata_activity);
    }

    //logica che gestisce il reset della password e verifica la correttezza dei dati inseriti
    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError(getResources().getString(R.string.email_richiesta));
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getResources().getString(R.string.email_valida));
            emailEditText.requestFocus();
            return;
        }

        if(!dataBaseHelper.checkEmailExist(email)){
            emailEditText.setError(getResources().getString(R.string.email_errata));
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
    //passa all'activity di reset della password attraverso un intent esplicito che contiene l'email del curatore
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(Curatore.Keys.CURATORE_KEY, email);
        startActivity(intent);

        progressBar.setVisibility(View.VISIBLE);


    }
}
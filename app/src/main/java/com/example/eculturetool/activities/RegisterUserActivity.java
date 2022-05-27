package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;

import java.util.Objects;


public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNome, editTextCognome, editTextEmail, editTextPassword;
    private TextView registerUser;
    private final int PASSWORD_LENGTH = 6;
    private ProgressBar progressBar;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        registerUser = findViewById(R.id.avantiButton);
        registerUser.setOnClickListener(this);

        editTextNome = findViewById(R.id.nomeCuratore);
        editTextCognome = findViewById(R.id.cognomeCuratore);
        editTextEmail = findViewById(R.id.emailCuratore);
        editTextPassword = findViewById(R.id.passwordCuratore);

        dataBaseHelper = new DataBaseHelper(this);

        progressBar = findViewById(R.id.progressBarRegister);
        progressBar.setVisibility(View.INVISIBLE);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.registrazione));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.avantiButton) {
            registerUser();
        }
    }

    private void registerUser() {

        String nome = editTextNome.getText().toString().trim();
        String cognome = editTextCognome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (nome.isEmpty()) {
            editTextNome.setError(getResources().getString(R.string.inserisci_nome));
            editTextNome.requestFocus();
            return;
        }

        if (cognome.isEmpty()) {
            editTextCognome.setError(getResources().getString(R.string.inserisci_cognome));
            editTextCognome.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getResources().getString(R.string.email_richiesta));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getResources().getString(R.string.email_valida));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getResources().getString(R.string.password_richiesta));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < PASSWORD_LENGTH) {
            editTextPassword.setError(getResources().getString(R.string.password_min_caratteri) +  PASSWORD_LENGTH  + getResources().getString(R.string.caratteri));
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if(dataBaseHelper.checkEmailExist(email)){
            editTextEmail.setError(getResources().getString(R.string.email_esistente));
            editTextEmail.requestFocus();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        Curatore curatore = new Curatore(nome, cognome, email, password, null);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Curatore.Keys.CURATORE_KEY, curatore);

        Intent intent = new Intent(this, CreazioneLuogoActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }


    //Prova per recuperare i dati quando si ritorna indietro dall'Activity CreazioneMuseoActivity
    //INIZIO
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText editTextNomeSavedState = findViewById(R.id.nomeCuratore);
        CharSequence charSequence = editTextNomeSavedState.getText();
        savedInstanceState.putCharSequence("MySavedData", charSequence);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence restoreData = savedInstanceState.getCharSequence("MySavedData");
        EditText myEditText = findViewById(R.id.nomeCuratore);
        myEditText.setText(restoreData);
    }
    //FINE
}



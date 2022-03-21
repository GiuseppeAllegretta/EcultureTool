package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Curatore;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextNome, editTextCognome, editTextEmail, editTextPassword;
    private TextView registerUser;
    private final int PASSWORD_LENGTH = 6;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.avantiButton);
        registerUser.setOnClickListener(this);

        editTextNome = (EditText) findViewById(R.id.nomeCuratore);
        editTextCognome = (EditText) findViewById(R.id.cognomeCuratore);
        editTextEmail = (EditText) findViewById(R.id.emailCuratore);
        editTextPassword = (EditText) findViewById(R.id.passwordCuratore);

        progressBar = findViewById(R.id.progressBarRegister);
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avantiButton:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        String nome = editTextNome.getText().toString().trim();
        String cognome = editTextCognome.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (nome.isEmpty()) {
            editTextNome.setError("Il nome è richiesto");
            editTextNome.requestFocus();
            return;
        }

        if (cognome.isEmpty()) {
            editTextCognome.setError("Il cognome è richiesto");
            editTextCognome.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("L'email è richiesta");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("L'email deve essere valida");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("La password è richiesta");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < PASSWORD_LENGTH) {
            editTextPassword.setError("La password deve avere almeno " + PASSWORD_LENGTH + " caratteri");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Curatore curatore = new Curatore(nome, cognome, email);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Curatore.Keys.CURATORE_KEY, curatore);
        bundle.putString(Curatore.Keys.PASSWORD_KEY, password);

        Intent intent = new Intent(this, CreazioneMuseoActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);

    }


    //Prova per recuperare i dati quando si ritorna indietro dall'Activity CreazioneMuseoActivity
    //INIZIO
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText editTextNomeSavedState = (EditText) findViewById(R.id.nomeCuratore);
        CharSequence charSequence = editTextNomeSavedState.getText();
        savedInstanceState.putCharSequence("MySavedData", charSequence);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence restoreData = savedInstanceState.getCharSequence("MySavedData");
        EditText myEditText = (EditText) findViewById(R.id.nomeCuratore);
        myEditText.setText(restoreData);
    }
    //FINE
}



package com.example.eculturetool.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.SessionManagement;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.utilities.LocaleHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    DataBaseHelper dataBaseHelper;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private ProgressBar progressBar;
    private TextView language_dialog, register, passwordDimenticata;
    private Context context;
    private int lang_selected;
    private static final String TAG = "EmailPassword";
    private final int PASSWORD_LENGTH = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registrati);
        register.setOnClickListener(this);

        signIn = findViewById(R.id.logInButton);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);

        passwordDimenticata = findViewById(R.id.passwordDimenticata);
        passwordDimenticata.setOnClickListener(this);

        //TextView con nome nella lingua selezionata
        language_dialog = findViewById(R.id.dialog_language);

        //Riferimento al db SQLite
        dataBaseHelper = new DataBaseHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registrati:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;

            case R.id.logInButton:
                userLogin();
                break;

            case R.id.passwordDimenticata:
                startActivity(new Intent(this, PasswordDimenticataActivity.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
            editTextPassword.setError(getResources().getString(R.string.password_min_caratteri) + PASSWORD_LENGTH + getResources().getString(R.string.caratteri));
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if(dataBaseHelper.login(email, password)){
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.autenticazione_corretta), Toast.LENGTH_SHORT).show();

            //Gestione sessione
            SessionManagement sessionManagement=new SessionManagement(LoginActivity.this);
            sessionManagement.saveSession(email);

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

            progressBar.setVisibility(View.INVISIBLE);
            finish();
        }else {

            if(!dataBaseHelper.checkEmailExist(email)){
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.verifica_email), Toast.LENGTH_SHORT).show();
                editTextEmail.requestFocus();
                editTextEmail.setError(getString(R.string.email_errata));
                progressBar.setVisibility(View.INVISIBLE);
            }else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.autenticazione_fallita), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (LocaleHelper.getLanguage(getApplicationContext()).equalsIgnoreCase("it")) {
            context = LocaleHelper.setLocale(LoginActivity.this, "it");
            lang_selected = 0;
            language_dialog.setText("Italiano");
        } else if (LocaleHelper.getLanguage(getApplicationContext()).equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(LoginActivity.this, "en");
            lang_selected = 1;
            language_dialog.setText("English");
        }

        language_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //lista delle lingue disponibili
                final String[] language = {"Italiano", "Inglese"};

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                dialogBuilder.setTitle("Seleziona una lingua...")
                        .setSingleChoiceItems(language, lang_selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                language_dialog.setText(language[i]);

                                if (language[i].equals("Italiano")) {
                                    LocaleHelper.setLocale(LoginActivity.this, "it");
                                    lang_selected = 0;
                                }
                                if (language[i].equals("Inglese")) {
                                    LocaleHelper.setLocale(LoginActivity.this, "en");
                                    lang_selected = 1;
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                recreate();
                            }
                        });
                dialogBuilder.create().show();

            }
        });
    }


    public void popolaDBmodalitaOspite(View view) {




    }
}
package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private EditText emailEditText;
    private ProgressBar progressBar;
    private Button resetPasswordButton;
    private DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dimenticata);

        emailEditText = findViewById(R.id.emailReset);
        resetPasswordButton = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBarReset);
        progressBar.setVisibility(View.INVISIBLE);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        resetPasswordButton.setOnClickListener(onClickListener -> resetPassword());

        getSupportActionBar().setTitle(R.string.password_dimenticata_activity);
    }

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

        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra(Curatore.Keys.CURATORE_KEY, email); //passo la mail nell'intent
        startActivity(intent);

        progressBar.setVisibility(View.VISIBLE);


    }
}
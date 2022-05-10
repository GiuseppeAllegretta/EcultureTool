package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;

public class ResetPasswordActivity extends AppCompatActivity {

    private String email;
    private DataBaseHelper dataBaseHelper;
    private ImageView backImg, confirmImg;
    private EditText nuovaPassTxt, confirmPassTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        backImg = findViewById(R.id.freccia_back_reset_password);
        confirmImg = findViewById(R.id.icona_conferma_reset_password);
        nuovaPassTxt = findViewById(R.id.nuova_password_reset);
        confirmPassTxt = findViewById(R.id.conferma_nuova_password_reset);


        //Raccolgo i dati dall'intent
        Intent intent = getIntent();
        email = intent.getStringExtra(Curatore.Keys.CURATORE_KEY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        confirmImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificaPassword();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean modificaPassword() {

        boolean flagPassword = false;

        if (nuovaPassTxt.getText().toString().isEmpty()) {
            nuovaPassTxt.setError(getResources().getString(R.string.nuova_password));
            nuovaPassTxt.requestFocus();
            return false;
        }

        if (confirmPassTxt.getText().toString().isEmpty()) {
            confirmPassTxt.setError(getResources().getString(R.string.conferma_password));
            confirmPassTxt.requestFocus();
            return false;
        }

        if ((nuovaPassTxt.getText().toString().trim().compareTo(confirmPassTxt.getText().toString().trim())) != 0) {
            Toast.makeText(this, getResources().getString(R.string.password_non_coincidenti), Toast.LENGTH_SHORT).show();
            nuovaPassTxt.setError(getResources().getString(R.string.password_non_coincidenti));
            confirmPassTxt.setError(getResources().getString(R.string.password_non_coincidenti));
            nuovaPassTxt.requestFocus();
            return false;
        }

        if(dataBaseHelper.resetPassword(nuovaPassTxt.getText().toString(), email)){
            Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.password_aggiornata), Toast.LENGTH_SHORT).show();
            flagPassword = true;
            startActivity(new Intent(this, LoginActivity.class));
        }

        return flagPassword;
    }
}
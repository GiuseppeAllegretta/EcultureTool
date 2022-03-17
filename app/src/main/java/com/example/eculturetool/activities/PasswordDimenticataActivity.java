package com.example.eculturetool.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eculturetool.R;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordDimenticataActivity extends AppCompatActivity {

    private EditText emailEditText;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_dimenticata);

        emailEditText = findViewById(R.id.emailReset);
        Button resetPasswordButton = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBarReset);
        progressBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(onClickListener -> resetPassword());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("L'email è richiesta");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("L'email deve essere valida");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        auth.sendPasswordResetEmail(email).addOnCompleteListener(onCompleteListener -> {
            progressBar.setVisibility(View.INVISIBLE);

            if(onCompleteListener.isSuccessful()){
                Toast.makeText(PasswordDimenticataActivity.this, "Controlla la casella di posta", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PasswordDimenticataActivity.this, LoginActivity.class));
            }else{
                Toast.makeText(PasswordDimenticataActivity.this, "Prova di nuovo. Qualcosa è andato storto", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
package com.example.eculturetool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView register = findViewById(R.id.registrati);
        register.setOnClickListener(this);

        Button signIn = findViewById(R.id.logInButton);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.INVISIBLE);

        TextView passwordDimenticata = findViewById(R.id.passwordDimenticata);
        passwordDimenticata.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

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

        int PASSWORD_LENGTH = 6;
        if (password.length() < PASSWORD_LENGTH) {
            editTextPassword.setError("La password deve avere almeno " + PASSWORD_LENGTH + " caratteri");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener -> {
            if (onCompleteListener.isSuccessful()) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if (user.isEmailVerified()) {
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(LoginActivity.this, "Autenticazione corretta", Toast.LENGTH_SHORT).show();


                    //Gestione sessione
                    /*SessionManagement sessionManagement=new SessionManagement(LoginActivity.this);
                    System.out.println("UID -->>>>>" +user.getUid());
                    sessionManagement.saveSession(user.getUid());*/


                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Verifica l'email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", onCompleteListener.getException());
                Toast.makeText(LoginActivity.this, "Autenticazione fallita", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //checkSession();
    }

    /*private void checkSession() {
        SessionManagement sessionManagement=new SessionManagement(LoginActivity.this);
        String userID=sessionManagement.getSession();

        if(userID.compareTo("-1")!=0){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        }else{
            //non fa niente
        }
    }*/
}
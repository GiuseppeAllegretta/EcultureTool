package com.example.eculturetool.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eculturetool.R;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;

/**
 * Classe che consente all'utente di modificare la propria password
 */
public class ModificaPasswordActivity extends AppCompatActivity {

    /**
     * dataBaseHelper: istanza di DataBaseHelper, necessaria per reperire dati dal db
     * oldPassword: edit text per l'inserimento della password attuale
     * newPassword: edit text per l'inserimento della nuova password
     * confirmPassword: edit text per reinserire la nuova password al fine di controllare che combacino
     * progressBar: barra grafica che indica il progresso dell'operazione
     * passNuova: stringa contenente la nuova password impostata
     * curatore: contiene i dati relativi all'utente loggato
     */
    private DataBaseHelper dataBaseHelper;
    private EditText oldPassword, newPassword, confirmPassword;
    private ProgressBar progressBar;
    private String passNuova;
    private Curatore curatore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        oldPassword = findViewById(R.id.password_attuale);
        newPassword = findViewById(R.id.nuova_password);
        confirmPassword = findViewById(R.id.conferma_nuova_password);
        ImageView frecciaBack = findViewById(R.id.freccia_back_modifica_password);
        ImageView tastoConferma = findViewById(R.id.icona_conferma_modifica_password);
        progressBar = findViewById(R.id.pb);
        dataBaseHelper = new DataBaseHelper(this);
        curatore = dataBaseHelper.getCuratore();

        frecciaBack.setOnClickListener(onClickListener -> onBackPressed());

        tastoConferma.setOnClickListener(onClickListener -> {
            if (modificaPassword())
                onBackPressed();
        });
    }

    /**
     * Permette di modificare la password attuale, effettuando controlli sulla validità dell'input utente
     * @return true se l'operazione va a buon fine, false altrimenti
     */
    public boolean modificaPassword() {
        String passAttuale, passNuovaConf;
        passAttuale = oldPassword.getText().toString();
        passNuova = newPassword.getText().toString();
        passNuovaConf = confirmPassword.getText().toString();

        //gestione del caso in cui il campo password attuale  è vuoto
        if (passAttuale.isEmpty()) {
            oldPassword.setError(getResources().getString(R.string.password_attuale));
            oldPassword.requestFocus();
            return false;
        }

        //gestione del caso in cui il campo nuova password  è vuoto
        if (passNuova.isEmpty()) {
            newPassword.setError(getResources().getString(R.string.nuova_password));
            newPassword.requestFocus();
            return false;
        }

        //gestione del caso in cui il campo conferma password è vuoto
        if (passNuovaConf.isEmpty()) {
            confirmPassword.setError(getResources().getString(R.string.conferma_password));
            confirmPassword.requestFocus();
            return false;
        }

        //verifico che la nuova passowrd e la conferma della nuova password siano uguali o meno
        if (passNuova.compareTo(passNuovaConf) != 0) {
            Toast.makeText(this, getResources().getString(R.string.password_non_coincidenti), Toast.LENGTH_SHORT).show();
            newPassword.setError(getResources().getString(R.string.password_non_coincidenti));
            confirmPassword.setError(getResources().getString(R.string.password_non_coincidenti));
            newPassword.requestFocus();
            return false;
        }

        //Aggiorno la password
        return passwordVerificata(passAttuale, curatore.getPassword());
    }

    /**
     * Permette di verificare che la password inserita sia coerente
     * @param passwordAttuale, la password attualmente in uso
     * @param password, la nuova password da impostare
     * @return true se se due password coincidono, false altrimenti
     */
    public boolean passwordVerificata(String passwordAttuale, String password) {
        progressBar.setVisibility(View.VISIBLE);

        boolean flagPassword;
        if(passwordAttuale.compareTo(password) == 0){
            //verifica che l'aggiornamento sia eseguito correttamente
            if(dataBaseHelper.updatePassword(passNuova)){
                Toast.makeText(ModificaPasswordActivity.this, getResources().getString(R.string.password_aggiornata), Toast.LENGTH_SHORT).show();
                flagPassword = true;
            }else {
                Toast.makeText(ModificaPasswordActivity.this, getResources().getString(R.string.password_non_aggiornata), Toast.LENGTH_SHORT).show();
                flagPassword = false;
            }
        }else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_non_coincide), Toast.LENGTH_SHORT).show();
            oldPassword.setError(getString(R.string.password_non_corretta));
            oldPassword.requestFocus();
            flagPassword = false;
        }
        progressBar.setVisibility(View.INVISIBLE);

        curatore = dataBaseHelper.getCuratore();
        return flagPassword;
    }
}
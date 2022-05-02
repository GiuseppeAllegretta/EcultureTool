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
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Tipologia;
import com.example.eculturetool.entities.TipologiaOggetto;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utilities.LocaleHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

        //Stringhe relative alle zone del museo
        final String dipintiItaliani = "Dipinti Italiani";
        final String antichitàGrecheEtruscheRomane = "Antichità greche, etrusche e romane";
        final String sculture = "Sculture";
        final String antichitàEgizie = "Antichità Egizie";
        final String artiGrafiche = "Arti grafiche";

        final String emailOspite = "admin@gmail.com";
        final String passwordOspite = "123456";


        if(!dataBaseHelper.checkEmailExist(emailOspite)){
            //Creazione Curatore
            Curatore curatore = new Curatore("Ospite", "", emailOspite, "123456", null, -1);
            dataBaseHelper.addCuratore(curatore);

            //effettuo login
            //dataBaseHelper.login("admin", "123456");
            dataBaseHelper.setEmailCuratore(emailOspite);


            //Creazione Luogo
            Luogo museoLouvre = new Luogo("Museo del Louvre", "Il museo del Louvre di Parigi, in Francia, è uno dei più celebri musei del mondo e il primo per numero di visitatori. Si trova sulla rive droite, nel I arrondissement, tra la Senna e rue de Rivoli. L'accesso principale al museo è la Hall Napoléon sotto la piramide, dove si trovano le biglietterie e gli accessi alle tre ali del museo. La collezione del Museo del Louvre comprende oltre 380 000 oggetti e opere d'arte. Sono in esposizione permanente 35 000 opere, scelte dai curatori delle sue otto sezioni, ed esibite nei 60600 m² a loro dedicati", Tipologia.MUSEO, emailOspite);
            dataBaseHelper.addLuogo(museoLouvre);

            //valore fittizio assegnato
            int idLuogo = 0;

            List<Luogo> luoghi = dataBaseHelper.getLuoghi();
            for(Luogo luogo: luoghi){
                if(luogo.getEmailCuratore().compareTo(emailOspite) == 0){
                    idLuogo = luogo.getId();
                    dataBaseHelper.setLuogoCorrente(idLuogo);
                }
            }


            //Creazione Zone
            List<Zona> zone = new ArrayList<>();
            zone.add(new Zona(dipintiItaliani, "In quest'area si ha l'opportunità di ammirare opere dei pittori da Giotto, Carracci, Caravaggio, Raffaello,Tiziano, Cimabue e Veronese fino all'Ottocento.", idLuogo));
            zone.add(new Zona(antichitàGrecheEtruscheRomane, "Questo dipartimento del Louvre raccoglie le antichità di tre grandi civiltà: Roma, Grecia ed Etruria. Sono raccolte alcune delle opere più famose dell'arte di questi tre popoli, alcune simbolo universale di bellezza e che attirano visitatori e appassionati da tutto il mondo. Ecco alcune di queste opere divise per civiltà.", idLuogo));
            zone.add(new Zona(sculture, "In ques'area sono presenti le più importanti sculture da vedere dell'intero museo e che attirano l'attenzione dei visitatori", idLuogo));
            zone.add(new Zona(antichitàEgizie, " Dipartimento delle Antichità Egizie del Museo del Louvre conserva una delle principali collezioni egiziane del mondo fuori dal territorio egiziano, insieme al Museo Egizio di Torino e al British Museum e, in Egitto, al Museo Egizio del Cairo . La sua storia risale all'ordinanza reale di Carlo  X di15 maggio 1826", idLuogo));
            zone.add(new Zona(artiGrafiche, "l dipartimento di arti grafiche è il settimo dipartimento del museo del Louvre . È stata fondata nel 1989 . In precedenza l'ufficio di disegno era annesso al reparto di verniciatura .", idLuogo));

            for(Zona zona: zone){
                dataBaseHelper.aggiungiZona(zona);
            }

            //creazione oggetti
            zone = dataBaseHelper.getZone();

            for(Zona zona: zone){

                switch (zona.getNome()){
                    case dipintiItaliani:

                        dataBaseHelper.addOggetto(new Oggetto("Gioconda", "La Gioconda, nota anche come Monna Lisa, è un dipinto a olio su tavola di legno di pioppo realizzato da Leonardo da Vinci (77×53 cm e 13 mm di spessore), databile al 1503-1504 circa e conservato nel Museo del Louvre di Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F15296.jpg?alt=media&token=c8403eeb-838d-4497-b891-5af4015eaefa", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Le nozze di Cana", "Le Nozze di Cana è un dipinto di Paolo Caliari detto il Veronese del 1563, custodito al Louvre di Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FLe-nozze-di-Cana-Quadro-Veronese.jpg?alt=media&token=58a67022-5212-471d-9867-70918ddf6673", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Le vergine delle rocce", "La prima versione della Vergine delle Rocce è un dipinto a olio su tavola trasportato su tela (198x123 cm) di Leonardo da Vinci, databile al 1483-1486 e conservato nel Musée du Louvre di Parigi, mentre la seconda versione è conservata alla National Gallery di Londra.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fvergine%20delle%20rocce.jpg?alt=media&token=dfa2e6f8-92f0-4304-86c1-c51b2ba6edc9", TipologiaOggetto.QUADRO, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("San Sebastiano", "San Sebastiano è un dipinto, tempera su tela (257 × 142 cm), di Andrea Mantegna, databile al 1481 circa e conservata nel Museo del Louvre a Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2F205b5.jpg?alt=media&token=ec9663ef-d3ff-4d83-89dd-a58a4e69e99a", TipologiaOggetto.QUADRO, zona.getId()));

                        break;

                    case antichitàGrecheEtruscheRomane:
                        dataBaseHelper.addOggetto(new Oggetto("Venere di Milo", "L’Afrodite di Milo, meglio conosciuta come Venere di Milo, è una delle più celebri statue greche. Si tratta di una scultura di marmo pario alta 202 cm priva delle braccia e del basamento originale ed è conservata al Museo del Louvre di Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fvenere-milo-1200x711.jpg?alt=media&token=5071cdbd-27e6-4a8b-b8c2-b2fc030ce662", TipologiaOggetto.STATUA, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Schiavo morente", "Lo Schiavo morente è una scultura marmorea (h 215 cm) di Michelangelo, databile al 1513 circa e conservata nel Museo del Louvre a Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fschiavo-morente-1.jpg?alt=media&token=346a9843-ba89-493b-a502-5452997f0501", TipologiaOggetto.STATUA, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Nike di Samotracia", "La Nike di Samotracia è una scultura in marmo pario (h. 245 cm) di scuola rodia, dalla discussa attribuzione a Pitocrito, databile al 200-180 a.C. circa e oggi conservata al Museo del Louvre di Parigi.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FNike-di-Samotracia.jpg?alt=media&token=f7a531ce-0ab2-4bf4-a7b1-1c789e0b52e0", TipologiaOggetto.STATUA, zona.getId()));

                        break;

                    case sculture:
                        dataBaseHelper.addOggetto(new Oggetto("Amore e psiche", "Una famosa scultura in marmo, 'Psiche ravvivato da Cupido bacio', da Antonio Canova presso il Museo del Louvre, Parigi, Francia", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Famore-psiche-canova.jpg?alt=media&token=c220bb10-ce6e-463d-a4df-09d5c12c5fe0", TipologiaOggetto.SCULTURA, zona.getId()));

                        break;

                    case antichitàEgizie:
                        dataBaseHelper.addOggetto(new Oggetto("Lo scriba accovacciato", "Statua Calcare dipinto che rappresenta una scriba egiziano seduto a gambe incrociate, probabilmente risalente al IV ° o V ° dinastia . Trovato a Saqqarah nel 1850 da Auguste Mariette , in una tomba nel vicolo delle sfingi del Serapeo. Lo scriba, seduto a gambe incrociate su un piedistallo dipinto di nero, è rappresentato nell'atto di scrivere. È vestito con un perizoma bianco. Teneva un calamo nella mano destra e sul perizoma si vede un papiro parzialmente srotolato.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fscribe.jpg?alt=media&token=884cf322-5b23-4f88-8507-4b7b5452cce7", TipologiaOggetto.STATUA, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("Testa in quarzite di Djédefrê", "Questa testa di Djedefrê , figlio e successore di Cheope , alta 26 cm, in quarzite , con tracce di pittura, è più probabilmente un frammento di sfinge che una statua del re in piedi o seduto. Proviene dal sito della piramide in rovina di Djedefrê ad Abu Roach , pochi chilometri a nord delle piramidi di Giza .", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FDjedefre-head.jpg?alt=media&token=a05e9b5d-87ec-4923-9bf2-98209fa3bcba", TipologiaOggetto.STATUA, zona.getId()));
                        dataBaseHelper.addOggetto(new Oggetto("La Grande Sfinge di Tanis", "Il mostro dal volto reale, protegge la vita eterna dopo la morte ed è anche il guardiano immortale dei luoghi sacri! La sua posizione nel Louvre, lasciando la fortezza medievale, è impressionante, in quanto accoglie i visitatori nel dipartimento egizio del museo e alla scoperta dei suoi oggetti sacri.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2FPN9PPEM8louvre_obra_2.jpg?alt=media&token=b24e7f97-6911-4acc-9358-a820565ed212", TipologiaOggetto.STATUA, zona.getId()));

                        break;

                    case artiGrafiche:
                        dataBaseHelper.addOggetto(new Oggetto("La lotta per lo Stendardo", "Si tratta di una copia tratta dall'unica scena, quella centrale, dell'affresco con la Battaglia d'Anghiari eseguita da Leonardo nella sala del Consiglio di Palazzo Vecchio e andata distrutta verso il 1557 quando Vasari iniziò i lavori nel Palazzo.", "https://firebasestorage.googleapis.com/v0/b/auth-96a19.appspot.com/o/uploads%2Fobjects_images%2Fdownload.jpg?alt=media&token=9ea08f23-a428-4fdd-8005-72d2b10cb16e", TipologiaOggetto.QUADRO, zona.getId()));
                        break;
                }

            }


        }

        progressBar.setVisibility(View.VISIBLE);
        if(dataBaseHelper.login(emailOspite, passwordOspite)){
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.autenticazione_corretta), Toast.LENGTH_SHORT).show();

            //Gestione sessione
            SessionManagement sessionManagement=new SessionManagement(LoginActivity.this);
            sessionManagement.saveSession(emailOspite);

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

            progressBar.setVisibility(View.INVISIBLE);
            finish();
        }

    }
}
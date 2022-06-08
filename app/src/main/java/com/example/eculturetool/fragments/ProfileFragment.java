package com.example.eculturetool.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.activities.LoginActivity;
import com.example.eculturetool.activities.ModificaPasswordActivity;
import com.example.eculturetool.activities.ModificaProfiloActivity;
import com.example.eculturetool.activities.SplashActivity;
import com.example.eculturetool.activities.UploadImageActivity;
import com.example.eculturetool.activities.luoghi.LuoghiActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.SessionManagement;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.utilities.LocaleHelper;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * Fragment che si occupa di visualizzare le informazioni relative ad un utente:
 * foto profilo, e-mail, nome, cognome e luogo selezionato.
 * É possibile accedere alle funzioni:
 * Modifica password: per cambiare la password del proprio profilo
 * Elimina profilo: per eliminare il proprio profilo
 * Cambia lingua: per cambiare la lingua utilizzata per mostrare le informazioni all'interno dell'app
 * Cambia luogo: per cambiare il luogo selezionato
 * Esci: per uscire dal profilo corrente e ritornare alla schermata di login
 */
public class ProfileFragment extends Fragment {

    /**
     * Email dell'account ospite
     */
    private final String emailOspite = "admin@gmail.com";

    /**
     * Directory di Firebase Storage in cui verrà fatto l'upload delle immagini del profilo
     */
    public static final String PROFILE_IMAGES_DIR = "profile_images";

    /**
     * Informazioni globali dell'applicativo
     */
    private Context context;

    /**
     * Launcher che gestisce l'upload di un'immagine come immagine del profilo'
     */
    private ActivityResultLauncher<Intent> startForProfileImageUpload;

    /**
     * L'uri dell'immagine caricata
     */
    private Uri imgUri;

    /**
     * Oggetto curatore di cui si vogliono visualizzare le informazioni
     */
    protected static Curatore curatore;

    /**
     * La lingua corrente
     */
    private int lang_selected;

    /**
     * Istanza di Permissions
     */
    private Permissions permissions;

    /**
     * Istanza di DatabaseHelper
     */
    private DataBaseHelper dataBaseHelper;

    /**
     * Riferimenti
     */
    private TextView nomeFoto, email, nome, cognome, nomeLuogo;

    /**
     * Riferimento al pulsante per cambiare luogo selezionato
     */
    private Button cambiaLuogo;

    /**
     * Riferimento alla ImageView contenente l'immagine del profilo
     */
    private ImageView imgUser;

    /**
     * Riferimento alla progress bar
     */
    private ProgressBar progressBar;

    /**
     * Riferimenti ai pulsanti per cambiare immagine del profilo e per modificare il profilo
     */
    private FloatingActionButton changeImg, editButton;

    /**
     * Riferimento al pulsante per accedere alle impostazioni
     */
    private ImageButton settingsButton;

    /**
     * Costruttore aparametrico
     */
    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity().getApplicationContext();

        //Inizializzazione classe per la gestione dei permessi
        permissions = new Permissions();

        //Inizializzazione del database
        dataBaseHelper = new DataBaseHelper(requireActivity().getApplicationContext());

        //Launcher che gestisce l'upload di una nuova immagine del profilo
        startForProfileImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        //Controllo che l'uri ottenuto non sia nullo
                        if (uri != null) {
                            //aggiorno l'uri dell'immagine del curatore nel database
                            imgUri = uri;
                            dataBaseHelper.setImageCuratore(imgUri.toString());
                            //Controllo la presenza di connessione ad internet per il caricamento dell'immagine
                            if(permissions.checkConnection(context)){
                                Glide.with(context).load(imgUri).circleCrop().into(imgUser);
                            }else{
                                //In assenza di connessione ad internet viene caricato un apposito placeholder per segnalare l'evento
                                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.image_not_found)).circleCrop().into(imgUser);
                            }
                        }
                    }
                });
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate del layout per questo fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress);
        Button logout = view.findViewById(R.id.logout);
        imgUser = view.findViewById(R.id.imgUser);
        changeImg = view.findViewById(R.id.change_imgUser);
        nomeFoto = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        nome = view.findViewById(R.id.nome_profilo);
        cognome = view.findViewById(R.id.cognome_profilo);
        nomeLuogo = view.findViewById(R.id.luogo_selezionato);
        cambiaLuogo = view.findViewById(R.id.cambia_luogo);
        settingsButton = view.findViewById(R.id.settings_button);
        editButton = view.findViewById(R.id.fab);

        editButton.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), ModificaProfiloActivity.class)));

        //Popolo opportunamente i vari campi con i dati del curatore
        popolaCampi();

        //Listner del bottone per eseguire l'upload di una nuova immagine del profilo
        changeImg.setOnClickListener(onClickListener -> {
            //Controllo la presenza di connessione ad internet per il caricamento dell'immagine
            if(permissions.checkConnection(context)){
                Intent uploadImageIntent = new Intent(getActivity(), UploadImageActivity.class);
                //Imposto la directory di Firebase in cui verrà caricata l'immagine del profilo
                uploadImageIntent.putExtra("directory", PROFILE_IMAGES_DIR);
                startForProfileImageUpload.launch(uploadImageIntent);
            }else{
                //Segnalo all'utente con una Snackbar l'impossibilità di eseguire questa funzione in assenza di connessione ad internet
                Snackbar snackBar = permissions.getPermanentSnackBarWithOkAction(requireActivity().findViewById(R.id.frame_layout), getResources().getString(R.string.msg_internet_non_disponibile));
                snackBar.show();
            }
        });

        //Listner del bottone per cambiare luogo selezionato
        cambiaLuogo.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), LuoghiActivity.class)));

        //Listner del bottone per visualizzare le impostazioni dell'app
        settingsButton.setOnClickListener(this::showPopup);

        //Listner del bottone per eseguire il logout dall'app
        logout.setOnClickListener(onClickListener -> logout());

        //Nascondo i campi che un utente ospite non deve poter visualizzare
        nascondiView();
    }

    /**
     * Metodo per popolare opportunamente i vari campi con i dati del curatore.
     */
    private void popolaCampi() {
        //Ottengo i dati del curatore dal database
        Curatore curatore = dataBaseHelper.getCuratore();

        //Ottengo i dati del luogo selezionato dal database
        Luogo luogo = dataBaseHelper.getLuogoCorrente();

        //Imposto i campi opportunamente considerando la possibilità che possa essere un account ospite
        if (curatore.getEmail().compareTo(emailOspite) == 0) {
            nomeFoto.setText(curatore.getNome());
        } else {
            nomeFoto.setText(curatore.getNome() + " " + curatore.getCognome());
        }

        if (curatore.getEmail().compareTo(emailOspite) == 0){
            email.setText("**********@gmail.com");
        }else{
            email.setText(curatore.getEmail());
        }
        nome.setText(curatore.getNome());
        cognome.setText(curatore.getCognome());

        //Controllo che esista almeno un luogo selezionato
        if (luogo != null) {
            nomeLuogo.setText(luogo.getNome());
        }

        //Controllo che sia stata caricata almeno un'immagine del profilo
        if (curatore.getImg() != null) {
            //Controllo la presenza di connessione ad internet per il download dell'immagine
            if(permissions.checkConnection(context)){
                //Scarico l'immagine utilizzando l'url e la imposto nella ImageView
                Glide.with(context).load(curatore.getImg()).circleCrop().into(imgUser);
            }else{
                //In assenza di connessione ad internet viene caricato un apposito placeholder per segnalare l'evento
                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.image_not_found)).circleCrop().into(imgUser);
            }
        } else {
            //In assenza di immagine viene caricata un apposito placeholder
            progressBar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.ic_user).circleCrop().into(imgUser);
        }
    }

    /**
     * Metodo che esegue il logout dal profilo attualmente in uso.
     * L'utente, infine, viene riportato alla schermata per effettuare il login
     */
    public void logout() {
        //Elimino la sessione
        SessionManagement sessionManagement = new SessionManagement(requireActivity());
        sessionManagement.removeSession();

        //Imposto a null la variabile statica che indica il login di un curatore
        dataBaseHelper.setEmailCuratore(null);

        //Riporto l'utente alla schermata per effettuare il login
        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    /**
     * Metodo per visualizzare un menu a scoparsa contenente tutte le funzioni disponibili per impostare l'app
     * @param v la view
     */
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(onMenuItemClickListener -> {
            switch (onMenuItemClickListener.getItemId()) {
                case R.id.modifica_password_popup:
                    //Avvio l'activity per la modifica della password
                    startActivity(new Intent(getActivity(), ModificaPasswordActivity.class));
                    return true;
                case R.id.elimina_profilo_popup:
                    //Mostro dialog per confermare l'azione irreversibile
                    showConfirmDialog();
                    return true;

                case R.id.cambia_lingua_popup:
                    //Mostro dialog lingua corrente con opzione di modifica
                    gestioneLingua();
                    return true;

                default:
                    return false;
            }
        });
        popup.inflate(R.menu.settings_menu);
        popup.show();
    }

    /**
     * Metodo che si occupa di gestire la lingua dell'app e sue eventuali variazioni.
     * La scelta avviene mostrando un dialog all'utente in cui viene segnalata la lingua corrente
     * con la possibilità di cambiarla.
     */
    private void gestioneLingua() {
        //Determino la lingua corrente
        if (LocaleHelper.getLanguage(getContext()).equalsIgnoreCase("it")) {
            LocaleHelper.setLocale(getContext(), "it");
            lang_selected = 0;
        } else if (LocaleHelper.getLanguage(getContext()).equalsIgnoreCase("en")) {
            LocaleHelper.setLocale(getContext(), "en");
            lang_selected = 1;
        }

        //Lingue supportate dall'app
        final String[] Language = {"Italiano", "Inglese"};

        //Mostro all'utente il dialog con la lingua corrente
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.seleziona_lingua))
                //Offro all'utente la possibilità di cambiare la lingua nel dialog mostrato
                .setSingleChoiceItems(Language, lang_selected, (dialogInterface, i) -> {
                    if (Language[i].equals("Italiano")) {
                        LocaleHelper.setLocale(getContext(), "it");
                        lang_selected = 0;
                    }
                    if (Language[i].equals("Inglese")) {
                        LocaleHelper.setLocale(getContext(), "en");
                        lang_selected = 1;
                    }
                })
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    requireActivity().recreate();
                });
        dialogBuilder.create().show();
    }

    /**
     * Metodo che si occupa di generare e proporre all'utente un dialog di conferma per
     * l'eliminazione del profilo
     */
    void showConfirmDialog() {
        final Dialog dialog = new Dialog(getActivity());
        //Imposto opzioni dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        //inflate del layout del dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);
        //Imposto il testo del dialog
        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText(getResources().getString(R.string.cancella_profilo));

        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();

        //Listner del bottone per confermare l'azione irreversibile
        conferma.setOnClickListener(view -> {
            dialog.dismiss();
            //elimino il profilo del curatore
            if (dataBaseHelper.deleteCuratore()) {
                //cancello la sessione
                SessionManagement sessionManagement = new SessionManagement(requireActivity());
                sessionManagement.removeSession();
                //Informo l'utente dell'effettiva cancellazione avvenuta con successo
                Toast.makeText(context, getString(R.string.cancellazione_successo), Toast.LENGTH_SHORT).show();
                //Riporto l'utente alla splash activity
                startActivity(new Intent(getActivity(), SplashActivity.class));
                requireActivity().finish();
            } else {
                //Informo l'utente che qualcosa di inaspettato ha impedito la cancellazione del profilo
                Toast.makeText(context, getString(R.string.cancellazione_fallita), Toast.LENGTH_SHORT).show();
            }
        });

        //Listner del bottone per rifiutare l'azione irreversibile
        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }

    @Override
    public void onResume() {
        super.onResume();
        //Imposto opportunamente i vari campi con i dati del curatore
        popolaCampi();
    }

    /**
     * Metodo che si occupa di oscurare le informazioni che non devono essere visibili
     * ad un account di tipo ospite. Nello specifico, vengono resi irraggiungili i pulsanti per
     * la modifica dell'immagine del profilo, per la modifica del profilo, per modificare il luogo selezionato e
     * per accedere alle impostazioni dell'app
     */
    private void nascondiView() {
        String emailCuratore = dataBaseHelper.getCuratore().getEmail();

        if (emailCuratore.compareTo(emailOspite) == 0) {
            changeImg.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            cambiaLuogo.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.INVISIBLE);
        }
    }
}



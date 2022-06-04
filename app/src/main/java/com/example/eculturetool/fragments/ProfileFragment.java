package com.example.eculturetool.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.eculturetool.R;
import com.example.eculturetool.activities.LoginActivity;
import com.example.eculturetool.activities.LuoghiActivity;
import com.example.eculturetool.activities.ModificaPasswordActivity;
import com.example.eculturetool.activities.ModificaProfiloActivity;
import com.example.eculturetool.activities.SplashActivity;
import com.example.eculturetool.activities.UploadImageActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.database.SessionManagement;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.utilities.LocaleHelper;
import com.example.eculturetool.utilities.Permissions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {

    private final String emailOspite = "admin@gmail.com"; //email dell'account ospite

    public static final String PROFILE_IMAGES_DIR = "profile_images";
    private Context context;
    private ActivityResultLauncher<Intent> startForProfileImageUpload;
    private Uri imgUri;
    protected static Curatore curatore;
    private int lang_selected;
    private Permissions permissions;
    private DataBaseHelper dataBaseHelper;
    private TextView nomeFoto, email, nome, cognome, nomeLuogo;
    private Button cambiaLuogo;
    private ImageView imgUser;
    private ProgressBar progressBar;
    private FloatingActionButton changeImg, editButton;
    private ImageButton settingsButton;


    public ProfileFragment() {
        // Richiesto costruttore vuoto
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity().getApplicationContext();
        permissions = new Permissions();
        //Inizializzazione del database
        dataBaseHelper = new DataBaseHelper(requireActivity().getApplicationContext());

        startForProfileImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        //aggiorno l'url dell'immagine
                        if (uri != null) {
                            imgUri = uri;
                            dataBaseHelper.setImageCuratore(imgUri.toString());
                            if(permissions.checkConnection(context)){
                                Glide.with(context).load(imgUri).circleCrop().into(imgUser);
                            }else{
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

        //Popola i campi del profilo
        popolaCampi();

        changeImg.setOnClickListener(onClickListener -> {
            //Controllo la connessione
            if(permissions.checkConnection(context)){
                Intent uploadImageIntent = new Intent(getActivity(), UploadImageActivity.class);
                uploadImageIntent.putExtra("directory", PROFILE_IMAGES_DIR);
                startForProfileImageUpload.launch(uploadImageIntent);
            }else{
                Snackbar snackBar = permissions.getPermanentSnackBarWithOkAction(requireActivity().findViewById(R.id.frame_layout), getResources().getString(R.string.connessione_internet));
                snackBar.show();
            }
        });

        //Operazioni da eseguire quando l'utente decide di cambiare il luogo
        cambiaLuogo.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), LuoghiActivity.class)));

        settingsButton.setOnClickListener(this::showPopup);

        logout.setOnClickListener(onClickListener -> logout());

        nascondiView();
    }

    private void popolaCampi() {
        Curatore curatore = dataBaseHelper.getCuratore();
        Luogo luogo = dataBaseHelper.getLuogoCorrente();


        if (curatore.getEmail().compareTo(emailOspite) == 0) {
            nomeFoto.setText(curatore.getNome());
        } else {
            nomeFoto.setText(curatore.getNome() + " " + curatore.getCognome());
        }
        email.setText(curatore.getEmail());
        nome.setText(curatore.getNome());
        cognome.setText(curatore.getCognome());

        if (luogo != null) {
            nomeLuogo.setText(luogo.getNome());
        }

        if (curatore.getImg() != null) {
            if(permissions.checkConnection(context)){
                Glide.with(context).load(curatore.getImg()).circleCrop().into(imgUser);
            }else{
                Glide.with(context).load(AppCompatResources.getDrawable(context, R.drawable.image_not_found)).circleCrop().into(imgUser);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.ic_user).circleCrop().into(imgUser);
        }
    }

    public void logout() {
        //delete sessione
        SessionManagement sessionManagement = new SessionManagement(requireActivity());
        sessionManagement.removeSession();

        //Imposta a null la variabile statica che indica il login di un curatore
        dataBaseHelper.setEmailCuratore(null);

        startActivity(new Intent(getActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(onMenuItemClickListener -> {
            switch (onMenuItemClickListener.getItemId()) {
                case R.id.modifica_password_popup:
                    startActivity(new Intent(getActivity(), ModificaPasswordActivity.class));
                    return true;
                case R.id.elimina_profilo_popup:
                    showCustomDialog();
                    return true;

                case R.id.cambia_lingua_popup:
                    gestioneLingua();
                    return true;

                default:
                    return false;
            }
        });
        popup.inflate(R.menu.settings_menu);
        popup.show();
    }

    private void gestioneLingua() {

        if (LocaleHelper.getLanguage(getContext()).equalsIgnoreCase("it")) {
            LocaleHelper.setLocale(getContext(), "it");
            lang_selected = 0;
        } else if (LocaleHelper.getLanguage(getContext()).equalsIgnoreCase("en")) {
            LocaleHelper.setLocale(getContext(), "en");
            lang_selected = 1;
        }

        final String[] Language = {"Italiano", "Inglese"};
        Log.d("Clicked", "Clicked");

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.seleziona_lingua))
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


    // Metodo che gestisce il dialog di conferma eliminazione del profilo

    void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, null);
        dialog.setContentView(layout);

        TextView testo_tv = layout.findViewById(R.id.titolo_dialog);
        testo_tv.setText(getResources().getString(R.string.cancella_profilo));

        final Button conferma = dialog.findViewById(R.id.conferma);
        final Button rifiuto = dialog.findViewById(R.id.annulla);

        dialog.show();

        conferma.setOnClickListener(view -> {
            dialog.dismiss();

            if (dataBaseHelper.deleteCuratore()) {
                //cancella sessione
                SessionManagement sessionManagement = new SessionManagement(requireActivity());
                sessionManagement.removeSession();

                Toast.makeText(context, getString(R.string.cancellazione_successo), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SplashActivity.class));
                requireActivity().finish();
            } else {
                Toast.makeText(context, getString(R.string.cancellazione_fallita), Toast.LENGTH_SHORT).show();
            }

        });

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }

    @Override
    public void onResume() {
        super.onResume();
        popolaCampi();
    }


    // Nasconde alcune view nel caso in cui si faccia l'accesso con l'account ospite
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



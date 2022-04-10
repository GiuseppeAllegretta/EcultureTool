package com.example.eculturetool.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.database.SessionManagement;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.utilities.LocaleHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    public static final String PROFILE_IMAGES_DIR = "profile_images";
    private final Connection connection = new Connection();
    private Context context;
    private ActivityResultLauncher<Intent> startForProfileImageUpload;
    protected static Curatore curatore;
    private TextView nomeFoto, email, nome, cognome, nomeLuogo;
    private Button cambiaLuogo;
    private Toolbar myToolbar;
    private ImageView imgUser;
    private Uri imgUri;
    private ProgressBar progressBar;
    private int lang_selected;

    //Variabile che tiene traccia del luogo corrente. Sarà avvalorata quando si otterrano i riferimenti del curatore attraverso snapshot
    private String luogoCorrente;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity().getApplicationContext();
        startForProfileImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = null;
                    if (activityResult.getData() != null) {
                        uri = activityResult.getData().getData();
                    }
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK) {
                        //Riferimento a realtime database
                        FirebaseDatabase mFirebaseInstance = connection.getDatabase();
                        // get reference to 'curatori' node
                        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("curatori");
                        //aggiorno l'url dell'immagine
                        if (uri != null) {
                            imgUri = uri;
                            mFirebaseDatabase.child(connection.getUser().getUid()).child("img").setValue(imgUri.toString());
                            Glide.with(context).load(curatore.getImg()).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).circleCrop().into(imgUser);
                        }
                    }
                });
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress);
        myToolbar = view.findViewById(R.id.toolbarOggetto);
        Button logout = view.findViewById(R.id.logout);
        imgUser = view.findViewById(R.id.imgUser);
        FloatingActionButton changeImg = view.findViewById(R.id.change_imgUser);
        nomeFoto = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        nome = view.findViewById(R.id.nome_profilo);
        cognome = view.findViewById(R.id.cognome_profilo);
        nomeLuogo = view.findViewById(R.id.luogo_selezionato);
        cambiaLuogo = view.findViewById(R.id.cambia_luogo);
        ImageButton settingsButton = view.findViewById(R.id.settings_button);
        FloatingActionButton editButton = view.findViewById(R.id.fab);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ModificaProfiloActivity.class));
            }
        });

        DatabaseReference myRef = connection.getRefCuratore();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Curatore.class) != null) {
                    curatore = snapshot.getValue(Curatore.class);
                    if (curatore != null) {
                        nomeFoto.setText(String.format("%s %s", curatore.getNome(), curatore.getCognome()));
                        email.setText(curatore.getEmail());
                        nome.setText(curatore.getNome());
                        cognome.setText(curatore.getCognome());
                        luogoCorrente = curatore.getLuogoCorrente();
                    }

                    connection.getRefLuoghi().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(Luogo.class) != null) {
                                System.out.println("snapshot: " + snapshot);
                                Luogo luogo = snapshot.getValue(Luogo.class);
                                if (luogo != null) {
                                    System.out.println(luogo);
                                    nomeLuogo.setText(luogo.getNome());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if (curatore.getImg() != null) {
                        Glide.with(context).load(curatore.getImg()).circleCrop().into(imgUser);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Glide.with(context).load(R.drawable.ic_user).circleCrop().into(imgUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        changeImg.setOnClickListener(onClickListener -> {
            Intent uploadImageIntent = new Intent(getActivity(), UploadImageActivity.class);
            uploadImageIntent.putExtra("directory", PROFILE_IMAGES_DIR);
            startForProfileImageUpload.launch(uploadImageIntent);
        });

        //Operazioni da eseguire quando l'utente decide di cambiare il luogo
        cambiaLuogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LuoghiActivity.class));
            }
        });

        settingsButton.setOnClickListener(onClickListener -> showPopup(onClickListener));

        logout.setOnClickListener(onClickListener -> logout());
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout

        //delete sessione
        SessionManagement sessionManagement = new SessionManagement(getActivity());
        sessionManagement.removeSession();

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
        dialogBuilder.setTitle("Seleziona una lingua...")
                .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Language[i].equals("Italiano")) {
                            LocaleHelper.setLocale(getContext(), "it");
                            lang_selected = 0;
                        }
                        if (Language[i].equals("Inglese")) {
                            LocaleHelper.setLocale(getContext(), "en");
                            lang_selected = 1;
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getActivity().recreate();
                    }
                });
        dialogBuilder.create().show();
    }

    private void setLocale(String lang) {
        //inizialize resources
        Resources resources = getResources();
        //Inizialize metrics
        DisplayMetrics metrics = resources.getDisplayMetrics();
        //Inizialize configurations
        Configuration configuration = resources.getConfiguration();
        //inizialize Locale
        configuration.locale = new Locale(lang);
        //Update configurations
        resources.updateConfiguration(configuration, metrics);
        //notify configurations
        onConfigurationChanged(configuration);


    }


    /**
     * Metodo che gestisce il dialog di conferma eliminazione del profilo.
     * E' possibile confermare o rifiutare l'eliminazione del profilo attraverso gli appositi button
     */
    void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_elimina_profilo);

        final Button conferma = dialog.findViewById(R.id.conferma_cancellazione);
        final Button rifiuto = dialog.findViewById(R.id.annulla_cancellazione);

        //Serve per cancellare il nodo del rispettivo curatore dal Realtime database in quanto con il delete verrebbe
        //cancellata l'istanza del curatore. IN questo modo manteniamo l'uid per poter cancellare il curatore
        //successivamente all'eleminazione dello stesso nell'authentication db

        dialog.show();

        conferma.setOnClickListener(onClickListener -> connection.getUser().delete()
                .addOnCompleteListener(onCompleteListener -> {
                    if (onCompleteListener.isSuccessful()) {
                        connection.getRefCuratore().removeValue();
                        dialog.dismiss();

                        //delete sessione
                        SessionManagement sessionManagement = new SessionManagement(getActivity());
                        sessionManagement.removeSession();

                        startActivity(new Intent(getActivity(), SplashActivity.class));
                        requireActivity().finish();
                    }
                }));

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }
}



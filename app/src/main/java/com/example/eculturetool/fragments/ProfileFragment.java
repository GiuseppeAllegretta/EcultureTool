package com.example.eculturetool.fragments;

import android.app.Dialog;
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
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.LoginActivity;
import com.example.eculturetool.activities.ModificaPasswordActivity;
import com.example.eculturetool.activities.ModificaProfiloActivity;
import com.example.eculturetool.activities.SplashActivity;
import com.example.eculturetool.activities.UploadImageActivity;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.utilities.CircleTransform;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    public static final String PROFILE_IMAGES_DIR = "profile_images";
    private final Connection connection = new Connection();

    private ActivityResultLauncher<Intent> startForProfileImageUpload;
    protected static Curatore curatore;
    private TextView nomeFoto, email, nome, cognome;

    private ImageView imgUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startForProfileImageUpload = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    Uri uri = activityResult.getData().getData();
                    if (activityResult.getResultCode() == UploadImageActivity.RESULT_OK){
                        //Riferimento a realtime database
                        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
                        // get reference to 'curatori' node
                        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference("curatori");
                        //aggiorno l'url dell'immagine
                        mFirebaseDatabase.child(connection.getUser().getUid()).child("img").setValue(uri.toString());
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

        Button logout = view.findViewById(R.id.logout);
        imgUser = view.findViewById(R.id.imgUser);
        FloatingActionButton changeImg = view.findViewById(R.id.change_imgUser);
        nomeFoto = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        nome = view.findViewById(R.id.nome_profilo);
        cognome = view.findViewById(R.id.cognome_profilo);
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
                if(snapshot.getValue(Curatore.class) != null){
                    curatore = snapshot.getValue(Curatore.class);
                    if(curatore.getNome() != null && curatore.getCognome() != null)
                        nomeFoto.setText(curatore.getNome() + " " + curatore.getCognome());
                    email.setText(curatore.getEmail());
                    nome.setText(curatore.getNome());
                    cognome.setText(curatore.getCognome());
                    Picasso.get().load(curatore.getImg()).transform(new CircleTransform()).into(imgUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeImg.setOnClickListener(onClickListener -> {
            System.out.println("Button Clicked");
            Intent uploadImageIntent = new Intent(getActivity(), UploadImageActivity.class);
            uploadImageIntent.putExtra("directory", PROFILE_IMAGES_DIR);
            startForProfileImageUpload.launch(uploadImageIntent);
        });

        settingsButton.setOnClickListener(onClickListener -> showPopup(onClickListener));

        logout.setOnClickListener(onClickListener -> logout());

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
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

                default:
                    return false;
            }
        });
        popup.inflate(R.menu.settings_menu);
        popup.show();
    }

    /**
     * Metodo che gestisce il dialog di conferma eliminazione del profilo.
     * E' possibile confermare o rifiutare l'eliminazione del profilo attraverso gli appositi button
     */
    void showCustomDialog(){
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
                        startActivity(new Intent(getActivity(), SplashActivity.class));
                        requireActivity().finish();
                    }
                }));

        rifiuto.setOnClickListener(onClickListener -> dialog.dismiss());
    }
}



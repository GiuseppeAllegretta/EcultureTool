package com.example.eculturetool.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.HomeActivity;
import com.example.eculturetool.activities.LoginActivity;
import com.example.eculturetool.activities.ModificaPasswordActivity;
import com.example.eculturetool.activities.ModificaProfiloActivity;
import com.example.eculturetool.activities.UploadImageActivity;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.utilities.CircleTransform;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Connection connection = new Connection();
    private DatabaseReference myRef;

    private TextView nomeFoto, cognomeFoto, email, nome, cognome;
    private ProgressBar progressBar;
    private static final int GALLERY_INTENT_CODE = 4269;
    private ImageView profilePic;
    TextView label;
    ImageView imgUser;
    Button logout;
    FloatingActionButton changeImg;
    Activity context;
    FloatingActionButton editButton;
    String strImage;
    ImageButton settingsButton;
    Button eliminaProfilo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String imageUri;
    private Bitmap bitmap;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vistaProfilo = inflater.inflate(R.layout.fragment_profile, container, false);
        //((HomeAdminActivity) requireActivity()).disableBackArrow();

        imgUser = vistaProfilo.findViewById(R.id.imgUser);

        label = vistaProfilo.findViewById(R.id.profile_name);
        //label.setText(new StringBuilder().append(HomeActivity.loggedUser.getNome()).append(" ").append(HomeActivity.loggedUser.getCognome()).toString());

        //
        label = vistaProfilo.findViewById(R.id.profile_email);
        //label.setText(HomeActivity.loggedUser.getEmail());

        return vistaProfilo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout = view.findViewById(R.id.logout);
        imgUser = view.findViewById(R.id.imgUser);
        changeImg = view.findViewById(R.id.change_imgUser);
        nomeFoto = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        nome = view.findViewById(R.id.nome_profilo);
        cognome = view.findViewById(R.id.cognome_profilo);
        settingsButton = view.findViewById(R.id.settings_button);
        eliminaProfilo=view.findViewById(R.id.elimina_profilo_popup);



        editButton = view.findViewById(R.id.fab);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ModificaProfiloActivity.class));



                /*Intent intent = new Intent(context.getApplicationContext(), ModificaProfiloActivity.class);
                startActivity(intent);*/


            }
        });


        myRef = connection.getMyRefCuratore();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nomeFoto.setText(new StringBuilder().append(snapshot.getValue(Curatore.class).getNome()).append(" ").append(snapshot.getValue(Curatore.class).getCognome()).toString());
                email.setText(snapshot.getValue(Curatore.class).getEmail());
                nome.setText(snapshot.getValue(Curatore.class).getNome());
                cognome.setText(snapshot.getValue(Curatore.class).getCognome());

                //Picasso.get().load(snapshot.getValue(Curatore.class).getImg()).fit().centerCrop().into(imgUser);
                Picasso.get().load(snapshot.getValue(Curatore.class).getImg()).transform(new CircleTransform()).into(imgUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent uploadImageActivity = new Intent(getActivity(), UploadImageActivity.class);
                startActivity(uploadImageActivity);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modifica_password_popup:
                        startActivity(new Intent(getActivity(), ModificaPasswordActivity.class));
                        return true;
                    case R.id.elimina_profilo_popup:
                        showCustomDialog();
                        return true;
                    case R.id.item3:
                        Toast.makeText(getActivity(), "Item 3 clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.item4:
                        Toast.makeText(getActivity(), "Item 4 clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.settings_menu);
        popup.show();
    }


    void showCustomDialog(){
        final Dialog dialog=new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_elimina_profilo);
        dialog.show();
    }

}



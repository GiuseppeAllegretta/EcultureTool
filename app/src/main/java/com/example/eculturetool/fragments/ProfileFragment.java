package com.example.eculturetool.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.LoginActivity;
import com.example.eculturetool.activities.UploadImageActivity;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Connection connection = new Connection();
    private DatabaseReference myRef;

    private TextView nome, cognome, email;
    private ProgressBar progressBar;
    private static final int GALLERY_INTENT_CODE = 4269;
    private ImageView profilePic;
    TextView label;
    Button imgUser;
    Button logout;
    FloatingActionButton changeImg;
    Activity context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Uri imageUri;
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

        //imgUser = vistaProfilo.findViewById(R.id.imgUser);
        //imgUser.setImageResource(R.drawable.ic_user);

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
        changeImg = view.findViewById(R.id.change_imgUser);

        nome = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);

        myRef = connection.getMyRefCuratore();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nome.setText(new StringBuilder().append(snapshot.getValue(Curatore.class).getNome()).append(" ").append(snapshot.getValue(Curatore.class).getCognome()).toString());
                email.setText(snapshot.getValue(Curatore.class).getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Button Clicked");

                Intent activity2Intent = new Intent(getActivity(), UploadImageActivity.class);
                startActivity(activity2Intent);
            }
        });



    }
    public void logout (View view){
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
        getActivity().finish();
    }

}



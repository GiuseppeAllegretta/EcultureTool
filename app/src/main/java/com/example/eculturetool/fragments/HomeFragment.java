package com.example.eculturetool.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.LuoghiActivity;
import com.example.eculturetool.activities.PercorsiActivity;
import com.example.eculturetool.activities.ZoneActivity;
import com.example.eculturetool.activities.OggettiActivity;
import com.example.eculturetool.database.Connection;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private Connection connection = new Connection();
    private DatabaseReference myRef;

    private TextView tv, luogoGestito;
    private CardView percorsi, luoghi, zone, oggetti;
    private String luogoCorrente;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv = view.findViewById(R.id.nomeCuratore);
        percorsi = view.findViewById(R.id.percorsiCard);
        luoghi = view.findViewById(R.id.luoghiCard);
        zone = view.findViewById(R.id.zoneCard);
        oggetti = view.findViewById(R.id.oggettiCard);
        luogoGestito = view.findViewById(R.id.nomeLuogoHome);

        myRef = connection.getRefCuratore();
        System.out.println("--->" + myRef);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("--->" + snapshot);
                //System.out.println(snapshot.getValue(Curatore.class).toString());
                if (snapshot.getValue(Curatore.class) != null) {
                    luogoCorrente = snapshot.getValue(Curatore.class).getLuogoCorrente();
                    tv.setText(snapshot.getValue(Curatore.class).getNome() + " " + snapshot.getValue(Curatore.class).getCognome());
                }

                connection.getRefLuoghi().child(luogoCorrente).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(Luogo.class) != null) {
                            System.out.println("snapshot: " + snapshot);
                            Luogo luogo = snapshot.getValue(Luogo.class);
                            System.out.println(luogo.toString());
                            luogoGestito.setText(Html.fromHtml(getString(R.string.stai_gestendo) + " " + "<b>" + luogo.getNome() + "</b>", 0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        percorsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PercorsiActivity.class));
            }
        });

        luoghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LuoghiActivity.class));
            }
        });

        oggetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OggettiActivity.class));
            }
        });

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ZoneActivity.class));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragments
        return inflater.inflate(R.layout.fragment_home, container, false);


    }


}
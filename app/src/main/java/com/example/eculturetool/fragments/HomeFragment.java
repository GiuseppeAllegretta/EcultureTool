package com.example.eculturetool.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.LuoghiActivity;
import com.example.eculturetool.activities.OggettiActivity;
import com.example.eculturetool.activities.PercorsiActivity;
import com.example.eculturetool.activities.ZoneActivity;
import com.example.eculturetool.database.DataBaseHelper;
import com.example.eculturetool.entities.Curatore;
import com.example.eculturetool.entities.Luogo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

public class HomeFragment extends Fragment {

    private FrameLayout frameLayout;
    private DataBaseHelper dataBaseHelper;
    private TextView tv, luogoGestito;
    private CardView percorsi, luoghi, zone, oggetti;
    private Context context;
    private ImageView showTutorial;
    private View view = null;


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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        showTutorial=view.findViewById(R.id.showTutorialHome);

        dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        dataBaseHelper.getCuratore();


        //Operazioni che settano il nome del curatore nella home
        Curatore curatore = dataBaseHelper.getCuratore();
        if(curatore != null){
            tv.setText(curatore.getNome() + " " + curatore.getCognome());
        }

        //Operazioni che settano il nome del luogo corrente nella home
        Luogo luogo = dataBaseHelper.getLuogoCorrente();
        if(luogo != null){
            luogoGestito.setText(Html.fromHtml(context.getString(R.string.stai_gestendo) + " " + "<b>" + luogo.getNome() + "</b>", 0));
        }

        showTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorial();
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
        frameLayout = new FrameLayout(getActivity());
        inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        frameLayout .addView(view);
        return frameLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Operazioni che settano il nome del luogo corrente nella home
        Luogo luogo = dataBaseHelper.getLuogoCorrente();
        if(luogo != null){
            luogoGestito.setText(Html.fromHtml(context.getString(R.string.stai_gestendo) + " " + "<b>" + luogo.getNome() + "</b>", 0));
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        frameLayout. removeAllViews();
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_home, null);
        frameLayout .addView(view);

    }



    private void showTutorial(){
        new TapTargetSequence(getActivity()).targets(
                TapTarget.forView(luoghi, getString(R.string.Pulsante_luoghi), getString(R.string.gestire_luoghi))
                        // All options below are optional
                        .outerCircleColor(R.color.gialloSecondario)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(15)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(12)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(false)
                        .transparentTarget(true)
                        .targetRadius(60),

                TapTarget.forView(zone,getString(R.string.Pulsante_zone),getString(R.string.gestire_zone))
                        // All options below are optional
                        .outerCircleColor(R.color.gialloSecondario)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(15)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(12)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(false)
                        .transparentTarget(true)
                        .targetRadius(75),

                TapTarget.forView(oggetti,getString(R.string.Pulsante_oggetti),getString(R.string.gestire_oggetti))
                        // All options below are optional
                        .outerCircleColor(R.color.gialloSecondario)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(15)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(12)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(false)
                        .transparentTarget(true)
                        .targetRadius(60),
                TapTarget.forView(percorsi,getString(R.string.Pulsante_percorsi),getString(R.string.gestire_luoghi))
                        // All options below are optional
                        .outerCircleColor(R.color.gialloSecondario)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(15)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(12)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.black)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(false)
                        .tintTarget(false)
                        .transparentTarget(true)
                        .targetRadius(75))
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }).start();

    }

}
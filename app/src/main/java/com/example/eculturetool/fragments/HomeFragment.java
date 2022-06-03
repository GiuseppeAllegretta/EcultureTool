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

    private CardView percorsi, luoghi, zone, oggetti;
    private Context context;
    private Curatore curatore;
    private Luogo luogo;
    private final String emailOspite = "admin@gmail.com";


    public HomeFragment() {
        // Richiesto costruttore vuoto
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv = view.findViewById(R.id.nomeCuratore);
        percorsi = view.findViewById(R.id.percorsiCard);
        luoghi = view.findViewById(R.id.luoghiCard);
        zone = view.findViewById(R.id.zoneCard);
        oggetti = view.findViewById(R.id.oggettiCard);
        TextView luogoGestito = view.findViewById(R.id.nomeLuogoHome);
        ImageView showTutorial = view.findViewById(R.id.showTutorialHome);

        //Operazioni che settano il nome del curatore nella home
        if(curatore != null){
            if(curatore.getEmail().compareTo(emailOspite) == 0){
                tv.setText(curatore.getNome());
            }else{
                tv.setText(curatore.getNome() + " " + curatore.getCognome());
            }
        }
        //Operazioni che settano il nome del luogo corrente nella home
        if(luogo != null){
            luogoGestito.setText(Html.fromHtml(context.getString(R.string.stai_gestendo) + " " + "<b>" + luogo.getNome() + "</b>", 0));
        }

        showTutorial.setOnClickListener(view1 -> showTutorial());

        percorsi.setOnClickListener(view12 -> startActivity(new Intent(getActivity(), PercorsiActivity.class)));

        luoghi.setOnClickListener(view13 -> startActivity(new Intent(getActivity(), LuoghiActivity.class)));

        oggetti.setOnClickListener(view14 -> startActivity(new Intent(getActivity(), OggettiActivity.class)));

        zone.setOnClickListener(view15 -> startActivity(new Intent(getActivity(), ZoneActivity.class)));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Curatore", curatore);
        outState.putSerializable("Luogo", luogo);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            curatore = (Curatore) savedInstanceState.getSerializable("Curatore");
            luogo = (Luogo) savedInstanceState.getSerializable("Luogo");
        } else {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(requireActivity().getApplicationContext());
            dataBaseHelper.getCuratore();
            curatore = dataBaseHelper.getCuratore();
            luogo = dataBaseHelper.getLuogoCorrente();
        }

    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getActivity());
        inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        frameLayout.addView(view);
        return frameLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        requireActivity().recreate();

    }



    private void showTutorial(){
        new TapTargetSequence(requireActivity()).targets(
                TapTarget.forView(luoghi, getString(R.string.Pulsante_luoghi), getString(R.string.gestire_luoghi))
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
package com.example.eculturetool.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;

import com.example.eculturetool.R;

public class InfoZonaFragment extends Fragment {

    FragmentContainer fragmentContainer;

    public InfoZonaFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        //Ritorna la vista creata
        return layoutInflater.inflate(R.layout.fragment_info_zona, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView closeIcon = requireView().findViewById(R.id.ic_close);
        closeIcon.setOnClickListener(v -> {
            //Chiusura del fragment clickando l'icona per uscire
            getParentFragmentManager().beginTransaction().remove(InfoZonaFragment.this).commit();
        });

    }
}

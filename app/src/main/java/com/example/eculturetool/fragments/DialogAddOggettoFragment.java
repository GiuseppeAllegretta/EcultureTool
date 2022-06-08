package com.example.eculturetool.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.eculturetool.R;

/**
 * Classe che rappresenta il dialog che deve aprirsi nel momento in cui si vuole aggiungere un oggetto
 */
public class DialogAddOggettoFragment extends AppCompatDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Istanziazione dell'alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.informazione))
                .setMessage(getString(R.string.almeno_una_zona))
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });
        return builder.create();
    }
}

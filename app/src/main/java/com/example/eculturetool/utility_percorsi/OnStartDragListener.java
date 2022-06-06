package com.example.eculturetool.utility_percorsi;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Interfaccia per il trascinamento delle card nella creazione del percorso
 */
public interface OnStartDragListener {

    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}

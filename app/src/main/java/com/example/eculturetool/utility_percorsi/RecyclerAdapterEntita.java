package com.example.eculturetool.utility_percorsi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Entita;

import java.util.ArrayList;

public class RecyclerAdapterEntita extends RecyclerView.Adapter<RecyclerAdapterEntita.EntitaViewHolder> {
    private ArrayList<Entita> entitaList;

    public RecyclerAdapterEntita(ArrayList<Entita> entitaList) {
        this.entitaList=entitaList;

    }

    public class EntitaViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeEntita;
        private TextView descrizioneEntita;
        public EntitaViewHolder(final View view){
            super(view);
            nomeEntita= view.findViewById(R.id.nomeEntita);
            descrizioneEntita= view.findViewById(R.id.descrizioneEntita);
        }

    }
    @NonNull
    @Override
    public RecyclerAdapterEntita.EntitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_search,parent,false);
        return new EntitaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterEntita.EntitaViewHolder holder, int position) {
        String name = entitaList.get(position).getNome();
        String descrizione = entitaList.get(position).getDescrizione();
        holder.nomeEntita.setText(name);
        holder.descrizioneEntita.setText(descrizione);
    }

    @Override
    public int getItemCount() {
        return entitaList.size();
    }
}

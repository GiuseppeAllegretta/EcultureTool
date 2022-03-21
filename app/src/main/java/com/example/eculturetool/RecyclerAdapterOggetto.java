package com.example.eculturetool;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.entities.Oggetto;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class RecyclerAdapterOggetto extends RecyclerView.Adapter<RecyclerAdapterOggetto.OggettiViewHolder> {
    private ArrayList<Oggetto> oggettiList;

    public RecyclerAdapterOggetto(ArrayList<Oggetto> oggettiList){
        this.oggettiList=oggettiList;
    }
    public class OggettiViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeOggetto,descrizioneOggetto;
        //private ImageView immagineOggetto;


        public OggettiViewHolder(final View view){
            super(view);
            nomeOggetto=view.findViewById(R.id.nomeOggetto);
            descrizioneOggetto=view.findViewById(R.id.descrizioneOggetto);
            //immagineOggetto=view.findViewById(R.id.iconaOggetto);

        }
    }

    @NonNull
    @Override
    public RecyclerAdapterOggetto.OggettiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_oggetto, parent, false);
        return new OggettiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterOggetto.OggettiViewHolder holder, int position) {
        String nome=oggettiList.get(position).getNome();
        String descrizione=oggettiList.get(position).getDescrizione();
        holder.nomeOggetto.setText(nome);
        holder.descrizioneOggetto.setText(descrizione);
        //holder.immagineOggetto.setImageResource(oggetto.getUrl()); URL richiede int
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

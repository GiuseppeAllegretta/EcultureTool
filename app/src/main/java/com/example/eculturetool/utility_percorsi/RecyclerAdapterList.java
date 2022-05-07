package com.example.eculturetool.utility_percorsi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;

public class RecyclerAdapterList extends RecyclerView.Adapter<RecyclerAdapterList.ViewHolder>{

    ArrayList<Zona> listaZone;

    public RecyclerAdapterList(ArrayList<Zona> listaZone) {
        this.listaZone = listaZone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_zona, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nomeZona.setText(listaZone.get(position).getNome());
        holder.descrizioneZona.setText(listaZone.get(position).getDescrizione());
    }

    @Override
    public int getItemCount() {
        return listaZone.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nomeZona, descrizioneZona;
        //TODO impostare un numerino? RIMOSSO l'onClick

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            nomeZona = itemView.findViewById(R.id.nomeZona);
            descrizioneZona = itemView.findViewById(R.id.descrizioneZona);

        }

    }
}

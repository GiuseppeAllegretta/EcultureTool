package com.example.eculturetool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;

public class RecyclerAdapterZona extends RecyclerView.Adapter<RecyclerAdapterZona.ZoneViewHolder> {
    private ArrayList<Zona> zoneList;

    public RecyclerAdapterZona(ArrayList<Zona> zoneList){
        this.zoneList=zoneList;
    }

    public class ZoneViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeZona,descrizioneZona;
        public ZoneViewHolder(final View view){
            super(view);
            nomeZona=view.findViewById(R.id.nomeZona);
            descrizioneZona=view.findViewById(R.id.descrizioneZona);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterZona.ZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_zona,parent,false);
        return new ZoneViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterZona.ZoneViewHolder holder, int position) {
        String nome=zoneList.get(position).getNome();
        String descrizione=zoneList.get(position).getDescrizione();
        holder.nomeZona.setText(nome);
        holder.descrizioneZona.setText(descrizione);

    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }
}

package com.example.eculturetool.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Percorso;

import java.util.ArrayList;

public class RecyclerAdapterPercorso extends RecyclerView.Adapter<RecyclerAdapterPercorso.PercorsoViewHolder> {

    private ArrayList<Percorso> percorsiList;

    public RecyclerAdapterPercorso (ArrayList<Percorso> percorsiList){
        this.percorsiList = percorsiList;
    }

    public class PercorsoViewHolder extends RecyclerView.ViewHolder{
        private TextView nomePercorsoTxt;

        public PercorsoViewHolder(final View view){
            super(view);
            nomePercorsoTxt = view.findViewById(R.id.nomePercorso);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterPercorso.PercorsoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_percorsi, parent, false);
        return new PercorsoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterPercorso.PercorsoViewHolder holder, int position) {
        String nomePercorso = percorsiList.get(position).getNome();
        holder.nomePercorsoTxt.setText(nomePercorso);
    }

    @Override
    public int getItemCount() {
        return percorsiList.size();
    }
}

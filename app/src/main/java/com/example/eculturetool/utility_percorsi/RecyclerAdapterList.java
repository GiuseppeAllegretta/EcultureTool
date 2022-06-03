package com.example.eculturetool.utility_percorsi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Entita;

import java.util.ArrayList;

public class RecyclerAdapterList<T extends Entita> extends RecyclerView.Adapter<RecyclerAdapterList.ViewHolder>{

    ArrayList<T> list;

    public RecyclerAdapterList(ArrayList<T> list) {
        this.list = list;
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
        holder.nomeZona.setText(list.get(position).getNome());
        holder.descrizioneZona.setText(list.get(position).getDescrizione());

        //Nascondo immagine zona
        holder.imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, iconaFreccia;
        TextView nomeZona, descrizioneZona;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iconaZona);
            iconaFreccia = itemView.findViewById(R.id.icona_freccia);
            nomeZona = itemView.findViewById(R.id.nomeZona);
            descrizioneZona = itemView.findViewById(R.id.descrizioneZona);
        }
    }
}

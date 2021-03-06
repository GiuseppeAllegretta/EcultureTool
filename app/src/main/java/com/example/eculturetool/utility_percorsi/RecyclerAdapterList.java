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

/**
 * Recycler adapter di tipo lista, utilizzato per rappresentare una lista di oggetti di tipo Entita (Luoghi, Zone, Oggetti)
 * @param <T>, oggetto di tipo derivato da Entita
 */
public class RecyclerAdapterList<T extends Entita> extends RecyclerView.Adapter<RecyclerAdapterList.ViewHolder>{

    ArrayList<T> list;

    /**
     * Costruttore della recycler
     * @param list, la lista di oggetti che comporranno la recycler
     */
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
        holder.iconaFreccia.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Classe utilizzata per contenere una view all'interno della recycler
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * imageView: contiene l'icona della zona
         * iconaFreccia: contiene l'icona della freccia per tornare indietro
         * nomeZona: textview contenente il nome della zona
         * descrizioneZona: textview contenente la descrizione della zona
         */
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

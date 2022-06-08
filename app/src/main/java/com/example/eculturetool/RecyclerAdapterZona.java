package com.example.eculturetool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Recycler adapter di tipo lista, utilizzato per rappresentare una lista di zone
 */
public class RecyclerAdapterZona extends RecyclerView.Adapter<RecyclerAdapterZona.ZoneViewHolder> implements Filterable {

    /**
     * zoneList: array di oggetti di tipo Zona
     * mOnZonaListener: listener di un oggetto Zona, utilizzato per performare il movimento
     * zoneListAll: array che contiene tutte le zone, utilizzato per la ricerca
     */
    private ArrayList<Zona> zoneList;
    private OnZonaListener mOnZonaListener;
    private ArrayList<Zona> zoneListAll;

    /**
     * Costruttore di RecyclerAdapterZona
     * @param zoneList, la lista di zone
     * @param onZonaListener, il listener per la zona
     */
    public RecyclerAdapterZona(ArrayList<Zona> zoneList, OnZonaListener onZonaListener) {
        this.zoneList = zoneList;
        this.mOnZonaListener = onZonaListener;
        this.zoneListAll = new ArrayList<>(zoneList);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //eseguita sul thread in background
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Zona> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(zoneListAll);
            } else {
                for (Zona zona : zoneListAll) {
                    if (zona.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(zona);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //eseguito sul UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            zoneList.clear();
            zoneList.addAll((Collection<? extends Zona>) filterResults.values);
            notifyDataSetChanged();

        }
    };


    /**
     * Classe utilizzata per contenere la view relativa ad una zona nella recycler
     */
    public class ZoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView nomeZona, descrizioneZona;
        private OnZonaListener onZonaListener;

        /**
         * Costruttore di LuoghiViewHolder
         * @param view, la vista di una zona
         * @param onZonaListener, il listener di una zona
         */
        public ZoneViewHolder(final View view, OnZonaListener onZonaListener) {
            super(view);
            nomeZona = view.findViewById(R.id.nomeZona);
            descrizioneZona = view.findViewById(R.id.descrizioneZona);
            this.onZonaListener = onZonaListener;

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onZonaListener.onZonaClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onZonaListener.onZonaLongClick(getAdapterPosition());
            return false;
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterZona.ZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_zona, parent, false);
        return new ZoneViewHolder(itemView, mOnZonaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterZona.ZoneViewHolder holder, int position) {
        String nome = zoneList.get(position).getNome();
        String descrizione = zoneList.get(position).getDescrizione();
        holder.nomeZona.setText(nome);
        holder.descrizioneZona.setText(descrizione);

    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public interface OnZonaListener {
        void onZonaClick(int position);

        void onZonaLongClick(int position);
    }
}

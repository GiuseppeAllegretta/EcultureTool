package com.example.eculturetool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapterZona extends RecyclerView.Adapter<RecyclerAdapterZona.ZoneViewHolder> implements Filterable {
    private ArrayList<Zona> zoneList;
    private OnZonaListener mOnZonaListener;
    private ArrayList<Zona> zoneListAll;


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
        //run on background thread
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

        //runs on a UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            zoneList.clear();
            zoneList.addAll((Collection<? extends Zona>) filterResults.values);
            notifyDataSetChanged();

        }
    };


    public class ZoneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView nomeZona, descrizioneZona;
        OnZonaListener onZonaListener;

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

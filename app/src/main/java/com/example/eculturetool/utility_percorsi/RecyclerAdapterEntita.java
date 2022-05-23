package com.example.eculturetool.utility_percorsi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Entita;

import java.util.ArrayList;
import java.util.Collection;

public class RecyclerAdapterEntita extends RecyclerView.Adapter<RecyclerAdapterEntita.EntitaViewHolder> implements Filterable {
    private ArrayList<Entita> entitaList;
    private ArrayList<Entita> entitaListAll;

    public RecyclerAdapterEntita(ArrayList<Entita> entitaList) {
        this.entitaList=entitaList;
        this.entitaListAll = new ArrayList<>(entitaList);

    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        //eseguito in background
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Entita> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(entitaListAll);
            }else{
                for(Entita e : entitaListAll){
                    if (e.getNome().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(e);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }
        //eseguito nell'ui
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            entitaList.clear();
            entitaList.addAll((Collection<? extends Entita>) results.values);
            notifyDataSetChanged();
        }
    };

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

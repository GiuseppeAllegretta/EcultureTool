package com.example.eculturetool.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Percorso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapterPercorso extends RecyclerView.Adapter<RecyclerAdapterPercorso.PercorsoViewHolder> implements Filterable {

    private ArrayList<Percorso> percorsiList;
    private ArrayList<Percorso> percorsiListAll;

    public RecyclerAdapterPercorso (ArrayList<Percorso> percorsiList){
        this.percorsiList = percorsiList;
        this.percorsiListAll = new ArrayList<>(percorsiList);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Percorso> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(percorsiListAll);
            }else {
                for(Percorso percorso: percorsiListAll){
                    if(percorso.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(percorso);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //runs on a un thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            percorsiList.clear();
            percorsiList.addAll((Collection<? extends Percorso>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

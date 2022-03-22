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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RecyclerAdapterOggetto extends RecyclerView.Adapter<RecyclerAdapterOggetto.OggettiViewHolder> implements Filterable {
    private ArrayList<Oggetto> oggettiList;
    private ArrayList<Oggetto> oggettiListAll;

    public RecyclerAdapterOggetto(ArrayList<Oggetto> oggettiList){
        this.oggettiList = oggettiList;
        this.oggettiListAll = new ArrayList<>(oggettiList);
    }


    public class OggettiViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeOggetto,descrizioneOggetto;
        //private ImageView immagineOggetto;


        public OggettiViewHolder(final View view){
            super(view);
            nomeOggetto = view.findViewById(R.id.nomeOggetto);
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
        String nome = oggettiList.get(position).getNome();
        String descrizione = oggettiList.get(position).getDescrizione();
        holder.nomeOggetto.setText(nome);
        holder.descrizioneOggetto.setText(descrizione);
        //holder.immagineOggetto.setImageResource(oggetto.getUrl()); URL richiede int
    }

    @Override
    public int getItemCount() {
        return oggettiList.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Oggetto> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(oggettiListAll);
            }else {
                for(Oggetto oggetto: oggettiListAll){
                    if(oggetto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(oggetto);
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
            oggettiList.clear();
            oggettiList.addAll((Collection<? extends Oggetto>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

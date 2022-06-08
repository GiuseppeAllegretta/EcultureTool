package com.example.eculturetool.utility_percorsi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eculturetool.R;
import com.example.eculturetool.entities.Entita;
import com.example.eculturetool.entities.Oggetto;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Recycle adapter di oggetti derivanti da Entita. Permette la ricerca di Luoghi, Zone ed Oggetti
 */
public class RecyclerAdapterEntita extends RecyclerView.Adapter<RecyclerAdapterEntita.EntitaViewHolder> implements Filterable {
    private ArrayList<Entita> entitaList;
    private ArrayList<Entita> entitaListAll;
    private OnEntitaListener mOnEntitaListener;

    /**
     * Costruttore del recycle adapter per entita
     * @param entitaList, una List di Entita
     * @param onEntitaListener, un listener di Entita
     */
    public RecyclerAdapterEntita(ArrayList<Entita> entitaList, OnEntitaListener onEntitaListener) {
        this.entitaList=entitaList;
        this.entitaListAll = new ArrayList<>(entitaList);
        this.mOnEntitaListener = onEntitaListener;
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

    /**
     * Classe utilizzata per contenere la view relativa ad un oggetto derivato da Entita nella recycler
     */
    public class EntitaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /**
         * nomeEntita: textview contenente il nome dell'entità
         * descrizioneEntita: textview contenente la descrizione dell'entità
         * onOggettoListener: listener custom per l'entità
         * immagineOggetto: imageview contenente l'immagine dell'entità
         */
        private TextView nomeEntita;
        private TextView descrizioneEntita;
        private OnEntitaListener onEntitaListener;
        private ImageView immagineEntita;

        /**
         * Costruttore ViewHolder
         * @param view, la vista
         * @param onEntitaListener, listener di Entita
         */
        public EntitaViewHolder(final View view, OnEntitaListener onEntitaListener){
            super(view);
            nomeEntita= view.findViewById(R.id.nomeEntita);
            descrizioneEntita= view.findViewById(R.id.descrizioneEntita);
            this.onEntitaListener = onEntitaListener;
            view.setOnClickListener(this);
            immagineEntita = view.findViewById(R.id.iconaEntita);
        }

        @Override
        public void onClick(View view) {
            onEntitaListener.onEntitaClick(getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public RecyclerAdapterEntita.EntitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_search,parent,false);
        return new EntitaViewHolder(itemView, mOnEntitaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterEntita.EntitaViewHolder holder, int position) {
        String name = entitaList.get(position).getNome();
        String descrizione = entitaList.get(position).getDescrizione();
        holder.nomeEntita.setText(name);
        holder.descrizioneEntita.setText(descrizione);

        if(entitaList.get(position) instanceof Oggetto)
            Glide.with(holder.immagineEntita.getContext()).load(((Oggetto) entitaList.get(position)).getUrl()).circleCrop().into(holder.immagineEntita);
    }

    @Override
    public int getItemCount() {
        return entitaList.size();
    }


    /**
     * Interfaccia che consente di selezionare una particolare entità presente nella view
     */
    public interface OnEntitaListener {

        void onEntitaClick(int position);
    }
}

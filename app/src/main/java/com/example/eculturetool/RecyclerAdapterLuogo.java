package com.example.eculturetool;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Recycler adapter di tipo lista, utilizzato per rappresentare una lista di luoghi
 */
public class RecyclerAdapterLuogo extends RecyclerView.Adapter<RecyclerAdapterLuogo.LuoghiViewHolder> implements Filterable {

    /**
     * luoghiList: array di oggetti di tipo Luogo
     * mOnLuogoListener: listener di un oggetto Luogo, utilizzato per performare il movimento
     * luoghiListAll: array che contiene tutti i luoghi, utilizzato per la ricerca
     */
    private ArrayList<Luogo> luoghiList;
    private OnLuogoListener mOnLuogoListener;
    private ArrayList<Luogo> luoghiListAll;

    /**
     * Costruttore di RecyclerAdapterLuogo
     * @param luoghiList, la lista di luoghi
     * @param onLuogoListener, il listener per il luogo
     */
    public RecyclerAdapterLuogo(ArrayList<Luogo> luoghiList, OnLuogoListener onLuogoListener) {
        this.luoghiList = luoghiList;
        this.mOnLuogoListener = onLuogoListener;
        this.luoghiListAll = new ArrayList<>(luoghiList);
    }


    /**
     * Classe utilizzata per contenere la view relativa ad un luogo nella recycler
     */
    public class LuoghiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * nomeLuogo: textview contenente il nome del luogo
         * iconaTipologia: imageview contenente l'immagine relativa alla tipologia del luogo
         * itemSelected: elemento della view selezionato
         * onLuogoListener: listener custom per il luogo
         */
        private TextView nomeLuogo;
        private ImageView iconaTipologia;
        private LinearLayout itemSelected;
        OnLuogoListener onLuogoListener;

        /**
         * Costruttore di LuoghiViewHolder
         * @param view, la vista di un luogo
         * @param onLuogoListener, il listener di un luogo
         */
        public LuoghiViewHolder(final View view, OnLuogoListener onLuogoListener) {
            super(view);
            nomeLuogo = view.findViewById(R.id.nomeLuogo);
            iconaTipologia = view.findViewById(R.id.iconaTipologia);
            itemSelected = view.findViewById(R.id.itemSelected);
            this.onLuogoListener = onLuogoListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onLuogoListener.onLuogoClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterLuogo.LuoghiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new LuoghiViewHolder(itemView, mOnLuogoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLuogo.LuoghiViewHolder holder, int position) {
        String nome = luoghiList.get(position).getNome();
        holder.nomeLuogo.setText(nome);
        setImageLuogo(holder, luoghiList.get(holder.getAdapterPosition()).getTipologia());
    }

    /**
     * Permette di impostare un'immagine al luogo a seconda della sua tipologia
     * @param holder, contenitore del luogo; contiene il luogo a cui dovrà essere impostata l'immagine
     * @param tipologia, la tipologia del luogo
     */
    private void setImageLuogo(RecyclerAdapterLuogo.LuoghiViewHolder holder, Tipologia tipologia) {
        switch (tipologia) {
            case MUSEO:
                holder.iconaTipologia.setImageResource(R.drawable.art_museum);
                break;

            case AREA_ARCHEOLOGICA:
                holder.iconaTipologia.setImageResource(R.drawable.archeology);
                break;

            case MOSTRA_ITINERANTE:
                holder.iconaTipologia.setImageResource(R.drawable.sculpture);
                break;

            case SITO_CULTURALE:
                holder.iconaTipologia.setImageResource(R.drawable.sweep);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return luoghiList.size();
    }

    public interface OnLuogoListener {
        void onLuogoClick(int position);
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    //Filtraggio dei luoghi
    Filter filter = new Filter() {
        //run on backgroun thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Luogo> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(luoghiListAll);
            } else {
                for (Luogo luogo : luoghiListAll) {
                    if (luogo.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(luogo);
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

            luoghiList.clear();
            luoghiList.addAll((Collection<? extends Luogo>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}

package com.example.eculturetool;


import static com.example.eculturetool.activities.oggetti.AggiungiOggettoActivity.PLACEHOLDER_OGGETTO;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eculturetool.entities.Oggetto;
import com.example.eculturetool.utilities.Permissions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Recycler adapter di tipo lista, utilizzato per rappresentare una lista di oggetti
 */
public class RecyclerAdapterOggetto extends RecyclerView.Adapter<RecyclerAdapterOggetto.OggettiViewHolder> implements Filterable {
    /**
     * oggettiList: array di oggetti di tipo Luogo
     * oggettiListAll: array che contiene tutti gli oggetti, utilizzato per la ricerca
     * mOnOggettoListener: listener di un oggetto Oggetto utilizzato per performare il movimento
     */
    private ArrayList<Oggetto> oggettiList;
    private ArrayList<Oggetto> oggettiListAll;
    private OnOggettoListener mOnOggettoListener;

    /**
     * Costruttore di RecyclerAdapterLuogo
     * @param oggettiList, la lista di oggetti
     * @param onOggettoListener, il listener per l'oggetto
     */
    public RecyclerAdapterOggetto(ArrayList<Oggetto> oggettiList, OnOggettoListener onOggettoListener) {
        this.oggettiList = oggettiList;
        this.oggettiListAll = new ArrayList<>(oggettiList);
        this.mOnOggettoListener = onOggettoListener;
    }

    /**
     * Classe utilizzata per contenere la view relativa ad un oggetto nella recycler
     */
    public class OggettiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        /**
         * nomeOggetto: textview contenente il nome dell'oggetto
         * descrizioneOggetto: textview contenente la descrizione dell'oggetto
         * immagineOggetto: imageview contenente l'immagine dell'oggetto
         * onOggettoListener: listener custom per l'oggetto
         */
        private TextView nomeOggetto, descrizioneOggetto;
        private ImageView immagineOggetto;
        private OnOggettoListener onOggettoListener;

        /**
         * Costruttore di OggettoViewHolder
         * @param view, la vista di un oggetto
         * @param onOggettoListener, il listener di un oggetto
         */
        public OggettiViewHolder(final View view, OnOggettoListener onOggettoListener) {
            super(view);
            nomeOggetto = view.findViewById(R.id.nomeOggetto);
            descrizioneOggetto = view.findViewById(R.id.descrizioneOggetto);
            immagineOggetto = view.findViewById(R.id.iconaOggetto);

            this.onOggettoListener = onOggettoListener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onOggettoListener.onOggettoClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onOggettoListener.onOggettoLongClick(getAdapterPosition());
            return false;
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterOggetto.OggettiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_oggetto, parent, false);
        return new OggettiViewHolder(itemView, mOnOggettoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterOggetto.OggettiViewHolder holder, int position) {

        String nome = oggettiList.get(position).getNome();
        String descrizione = oggettiList.get(position).getDescrizione();
        holder.nomeOggetto.setText(nome);
        holder.descrizioneOggetto.setText(descrizione);
        Permissions permissions = new Permissions();
        if (!Objects.equals(oggettiList.get(position).getUrl(), PLACEHOLDER_OGGETTO)) {
            if(permissions.checkConnection(holder.immagineOggetto.getContext())){
                Glide.with(holder.immagineOggetto.getContext()).load(oggettiList.get(position).getUrl()).circleCrop().into(holder.immagineOggetto);
            }else{
                Glide.with(holder.immagineOggetto.getContext()).load(AppCompatResources.getDrawable(holder.immagineOggetto.getContext(), R.drawable.image_not_found)).circleCrop().into(holder.immagineOggetto);
            }
        } else {
            holder.immagineOggetto.setImageDrawable(AppCompatResources.getDrawable(holder.immagineOggetto.getContext(), R.drawable.pottery));
        }
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

            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(oggettiListAll);
            } else {
                for (Oggetto oggetto : oggettiListAll) {
                    if (oggetto.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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


    public interface OnOggettoListener {
        void onOggettoClick(int position);

        void onOggettoLongClick(int position);
    }

}

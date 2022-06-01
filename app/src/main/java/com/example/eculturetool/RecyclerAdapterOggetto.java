package com.example.eculturetool;


import static com.example.eculturetool.activities.AggiungiOggettoActivity.PLACEHOLDER_OGGETTO;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class RecyclerAdapterOggetto extends RecyclerView.Adapter<RecyclerAdapterOggetto.OggettiViewHolder> implements Filterable {
    private ArrayList<Oggetto> oggettiList;
    private ArrayList<Oggetto> oggettiListAll;
    private OnOggettoListener mOnOggettoListener;

    public RecyclerAdapterOggetto(ArrayList<Oggetto> oggettiList, OnOggettoListener onOggettoListener) {
        this.oggettiList = oggettiList;
        this.oggettiListAll = new ArrayList<>(oggettiList);
        this.mOnOggettoListener = onOggettoListener;
    }


    public class OggettiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView nomeOggetto, descrizioneOggetto;
        private ImageView immagineOggetto;
        private OnOggettoListener onOggettoListener;


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
        if (!Objects.equals(oggettiList.get(position).getUrl(), PLACEHOLDER_OGGETTO)) {
            Glide.with(holder.immagineOggetto.getContext()).load(oggettiList.get(position).getUrl()).circleCrop().into(holder.immagineOggetto);
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

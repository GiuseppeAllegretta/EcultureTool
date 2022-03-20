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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LuoghiViewHolder> implements Filterable {

    private ArrayList<Luogo> luoghiList;
    private OnLuogoListener mOnLuogoListener;

    //Serve per la ricerca
    private ArrayList<Luogo> luoghiListAll;

    public RecyclerAdapter(ArrayList<Luogo> luoghiList, OnLuogoListener onLuogoListener){
        this.luoghiList = luoghiList;
        this.mOnLuogoListener = onLuogoListener;
        this.luoghiListAll = new ArrayList<>(luoghiList);
    }


    public class LuoghiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nomeLuogo;
        private ImageView iconaTipologia;
        private LinearLayout itemSelected;
        OnLuogoListener onLuogoListener;

        public LuoghiViewHolder(final View view, OnLuogoListener onLuogoListener){
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
    public RecyclerAdapter.LuoghiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new LuoghiViewHolder(itemView, mOnLuogoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.LuoghiViewHolder holder, int position) {
        String nome = luoghiList.get(position).getNome();
        holder.nomeLuogo.setText(nome);
        holder.iconaTipologia.setImageResource(luoghiList.get(position).getIdImage());
        setImageLuogo(holder,luoghiList.get(holder.getAdapterPosition()).getTipologia());

        /**
        holder.itemSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("elemento selezionato: " + luoghiList.get(holder.getAdapterPosition()).toString());


            }
        });

        **/
    }

    private void setImageLuogo(RecyclerAdapter.LuoghiViewHolder holder,Tipologia tipologia) {
        switch (tipologia){
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
        public void onLuogoClick(int position);
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on backgroun thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Luogo> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(luoghiListAll);
            }else {
                for (Luogo luogo: luoghiListAll){
                    if(luogo.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())){
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

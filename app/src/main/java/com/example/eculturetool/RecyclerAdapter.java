package com.example.eculturetool;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.entities.Luogo;
import com.example.eculturetool.entities.Tipologia;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LuoghiViewHolder>{

    private ArrayList<Luogo> luoghiList;

    public RecyclerAdapter(ArrayList<Luogo> luoghiList){
        this.luoghiList = luoghiList;
    }

    public class LuoghiViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeLuogo;
        private ImageView iconaTipologia;
        private LinearLayout itemSelected;

        public LuoghiViewHolder(final View view ){
            super(view);
            nomeLuogo = view.findViewById(R.id.nomeLuogo);
            iconaTipologia = view.findViewById(R.id.iconaTipologia);
            itemSelected = view.findViewById(R.id.itemSelected);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.LuoghiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new LuoghiViewHolder(itemView);
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

}

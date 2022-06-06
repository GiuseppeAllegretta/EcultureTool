package com.example.eculturetool.utility_percorsi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.CreazioneDiramazioneActivity;
import com.example.eculturetool.activities.InfoZonaActivity;
import com.example.eculturetool.activities.VisualizzaDiramazioneActivity;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Recycler dinamico a griglia delle card nella creazione percorso
 */
public class RecyclerAdapterGrid extends RecyclerView.Adapter<RecyclerAdapterGrid.MyViewHolder>
    implements ItemTouchHelperAdapter {

    private Context context;
    private ArrayList<Zona> listZone;
    private OnStartDragListener listener;
    private DataHolder data = DataHolder.getInstance();


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardZona)
        CardView cardZona;
        @BindView(R.id.cardNumber)
        TextView cardNumber;
        @BindView(R.id.cardTitle)
        TextView cardTitle;
        @BindView(R.id.cardDescription)
        TextView cardDescription;
        @BindView(R.id.ic_close)
        ImageView closeCard;
        @BindView(R.id.creaDiramazione)
        RelativeLayout creaDiramazione;
        @BindView(R.id.scrittaDiramazione)
        TextView scrittaDiramazione;
        @BindView(R.id.btnDiramazione)
        ImageView btnDiramazione;

        Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Costruttore del recycler
     * @param context
     * @param listZone
     * @param listener
     */
    public RecyclerAdapterGrid(Context context, ArrayList<Zona> listZone, OnStartDragListener listener) {
        this.context = context;
        this.listZone = listZone;
        this.listener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_card_creazione_percorso, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Binding di testo e numero di una card
        holder.cardNumber.setText(new StringBuilder().append(position+1));
        holder.cardTitle.setText(listZone.get(position).getNome());
        holder.cardDescription.setText(listZone.get(position).getDescrizione());
        if(data.getData().get(position).getDiramazione().isEmpty())
            holder.scrittaDiramazione.setText("Crea diramazione");
        else{
            holder.scrittaDiramazione.setText("Visualizza diramazione");
            holder.scrittaDiramazione.setGravity(Gravity.CENTER_VERTICAL);
            holder.btnDiramazione.setVisibility(View.GONE);
        }

        //Touch listener, differenzia i tipi di tocco
        holder.cardZona.setOnTouchListener(new View.OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    long eventDuration = e.getEventTime() - e.getDownTime();
                    //Se una card viene premuta per almeno 500ms può essere spostata
                    if (eventDuration > 500) {
                        listener.onStartDrag(holder);
                    }
                }

                //Eliminazione card
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if(holder.closeCard.getVisibility() == View.INVISIBLE)
                        holder.closeCard.setVisibility(View.VISIBLE);
                    else
                        holder.closeCard.setVisibility(View.INVISIBLE);
                    return super.onDoubleTap(e);
                }

                //Apertura info zona
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    openInfoZona(holder.cardTitle.getText().toString());
                    return true;
                }

            });


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        //Eliminazione card
        holder.closeCard.setOnClickListener(v -> removeAt(holder.getAdapterPosition()));

        //Area per la gestione della diramazione
        holder.creaDiramazione.setOnClickListener(v -> {
            ((Activity)context).finish();

            Intent intent;
            if(data.getData().get(position).getDiramazione().isEmpty())
                intent = new Intent(this.context, CreazioneDiramazioneActivity.class);
            else
                intent = new Intent(this.context, VisualizzaDiramazioneActivity.class);

            intent.putExtra("ROOT", holder.cardTitle.getText());
            intent.putExtra("NUMBER", holder.cardNumber.getText());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listZone.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(listZone, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        listZone.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position) {
        return listZone.get(position).hashCode();
    }


    /**
     * Nuova activity per vedere info di una zona
     */
    private void openInfoZona(String cardTitle){
        Intent intent = new Intent (context, InfoZonaActivity.class);
        intent.putExtra("TITLE", cardTitle);
        context.startActivity(intent);
    }

    /**
     * Rimuove la card passata come parametro
     * @param position, la posizione della card
     */
    public void removeAt(int position) {
        listZone.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listZone.size());
    }

}

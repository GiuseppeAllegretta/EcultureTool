package com.example.eculturetool.utility_percorsi;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
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
import com.example.eculturetool.activities.AddZonaToPercorsoActivity;
import com.example.eculturetool.entities.DataHolder;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.fragments.InfoZonaActivity;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>
    implements ItemTouchHelperAdapter {

    Context context;
    ArrayList<Zona> listZone;
    OnStartDragListener listener;


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
        Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }

    public MyRecyclerAdapter(Context context, ArrayList<Zona> listZone, OnStartDragListener listener) {
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

        //Touch listener, differenzia i tipi di tocco
        holder.cardZona.setOnTouchListener(new View.OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    long eventDuration = e.getEventTime() - e.getDownTime();
                    //Se una card viene premuta per almeno 800ms può essere spostata
                    if (eventDuration > 800) {
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
                    openInfoZona(holder.cardTitle, holder.cardDescription);
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

        //Area per creare la diramazione
        holder.creaDiramazione.setOnClickListener(v -> {
            DataHolder.getInstance().setData(listZone);
            Intent intent = new Intent (context, AddZonaToPercorsoActivity.class);
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
    private void openInfoZona(TextView title, TextView description){
        Intent intent = new Intent (context, InfoZonaActivity.class);
        intent.putExtra("TITLE", title.getText());
        intent.putExtra("DESCRIPTION", description.getText());
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

package com.example.eculturetool.utility_percorsi;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Zona;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>
    implements ItemTouchHelperAdapter {

    Context context;
    List<String> stringList;
    OnStartDragListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cardZona)
        CardView cardZona;
        @BindView(R.id.cardDescription)
        TextView cardText;
        @BindView(R.id.cardNumber)
        TextView cardNumber;

        Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
// TODO lista di int?
    public MyRecyclerAdapter(Context context, List<String> stringList, OnStartDragListener listener) {
        this.context = context;
        this.stringList = stringList;
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
        holder.cardText.setText(stringList.get(position));

        //Touch listener, differenzia i tipi di tocco
        holder.cardZona.setOnTouchListener(new View.OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    long eventDuration = e.getEventTime() - e.getDownTime();
                    //Se una card viene premuta per almeno 1s può essere spostata
                    if (eventDuration > 1000) {
                        listener.onStartDrag(holder);
                    }
                }

                //Apertura informazioni sul duoble tap
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Toast.makeText(context, "double tap", Toast.LENGTH_SHORT).show();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    Toast.makeText(context, "Nuovo thread", Toast.LENGTH_SHORT).show();
                    return super.onSingleTapConfirmed(e);
                }

                //TODO on single tap da impostare
            });



            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });


        //Recupero posizione della card
        holder.cardZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this){
                            //showDialog();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Nuovo thread", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //postDelayed se si vuole ritardardare l'aggiornamento della UI
                        //Log.i("boh", "waiting");
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(stringList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        stringList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position) {
        return stringList.get(position).hashCode();
    }
//TODO fare il dialog
    /*private void showDialog(){
        Dialog dialog = new Dialog(context.getApplicationContext());
        dialog.setContentView(R.layout.dialog_select_zona);
        dialog.show();
    }*/

}

package com.example.eculturetool.utility_percorsi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.activities.CreazionePercorsoActivity;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.fragments.InfoZonaActivity;
import com.example.eculturetool.fragments.InfoZonaFragment;
import com.example.eculturetool.fragments.ProfileFragment;

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
        @BindView(R.id.ic_close)
        ImageView closeCard;

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



                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    openInfoZona();
                    return true;
                }

            });



            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        //Clickando sulla singola card si aprono le info
        //TODO metterlo nel double tap?


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

    private void deleteCard(){

    }

    private void openInfoZona(){
        Intent intent = new Intent (context, InfoZonaActivity.class);
        context.startActivity(intent);
    }

}

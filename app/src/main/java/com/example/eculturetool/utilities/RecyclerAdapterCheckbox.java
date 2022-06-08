package com.example.eculturetool.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eculturetool.R;
import com.example.eculturetool.entities.Zona;
import com.example.eculturetool.utility_percorsi.CheckboxListener;

import java.util.ArrayList;

/**
 * Recycler adapter di tipo checkbox, utilizzato per selezionare le zone di una diramazione
 */
public class RecyclerAdapterCheckbox extends RecyclerView.Adapter<RecyclerAdapterCheckbox.ViewHolder> {

    /**
     * view: la vista della recycler
     * context: il contesto nel quale l'app si trova
     * itemList: lista contenente tutte le zone possibili per la diramazione
     * selectedList: lista contenente tutte e sole le zone selezionate
     * checkboxListener: listener per una checkbox
     */
    private View view;
    private Context context;
    private ArrayList<Zona> itemList;
    private ArrayList<Zona> selectedList = new ArrayList<>();
    private CheckboxListener checkboxListener;

    /**
     * Costruttore di RecyclerAdapterCheckbox
     * @param context, il contesto dell'app
     * @param itemList, la lista di tutte le zone
     * @param checkboxListener, listener per una checkbox
     */
    public RecyclerAdapterCheckbox(Context context, ArrayList<Zona> itemList, CheckboxListener checkboxListener) {
        this.context = context;
        this.itemList = itemList;
        this.checkboxListener = checkboxListener;
    }

    /**
     * Permette di recuperare una view
     * @return View, la view
     */
    public View getView() {
        return view;
    }

    /**
     * Permette di recuperare la lista delle zone sezionate
     * @return ArrayList, la lista
     */
    public ArrayList<Zona> getSelectedList(){
        return selectedList;
    }

    @NonNull
    @Override
    public RecyclerAdapterCheckbox.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.checkbox_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (itemList != null && itemList.size() > 0) {
            holder.checkBox.setText(itemList.get(position).getNome());
            holder.checkBox.setOnClickListener(v -> {
                if (holder.checkBox.isChecked()) {
                    selectedList.add(itemList.get(holder.getAdapterPosition()));
                } else {
                    selectedList.remove(itemList.get(holder.getAdapterPosition()));
                }
                checkboxListener.onQuantityChange(selectedList);
            });

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * Classe utilizzata per contenere la recycler view relativa alla schermata
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * checkBox: una checkbox rappresentante una zona selezionabile
         */
        CheckBox checkBox;

        /**
         * Costruttore di ViewHolder
         * @param view, la vista da contenere
         */
        public ViewHolder(@NonNull View view) {
            super(view);
            checkBox = view.findViewById(R.id.check_box);
        }
    }
}

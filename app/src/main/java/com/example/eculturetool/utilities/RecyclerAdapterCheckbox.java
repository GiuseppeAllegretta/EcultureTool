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

public class RecyclerAdapterCheckbox extends RecyclerView.Adapter<RecyclerAdapterCheckbox.ViewHolder> {

    View view;
    Context context;
    ArrayList<Zona> itemList;
    ArrayList<Zona> selectedList = new ArrayList<>();
    CheckboxListener checkboxListener;

    public RecyclerAdapterCheckbox(Context context, ArrayList<Zona> itemList, CheckboxListener checkboxListener) {
        this.context = context;
        this.itemList = itemList;
        this.checkboxListener = checkboxListener;
    }

    public View getView() {
        return view;
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View view) {
            super(view);
            checkBox = view.findViewById(R.id.check_box);
        }
    }
}

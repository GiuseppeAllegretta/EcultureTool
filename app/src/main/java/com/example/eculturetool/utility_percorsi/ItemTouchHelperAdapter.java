package com.example.eculturetool.utility_percorsi;

/**
 * custom item touch helper, utilizzato per la griglia contenente le zone nella crazione del percorso
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}

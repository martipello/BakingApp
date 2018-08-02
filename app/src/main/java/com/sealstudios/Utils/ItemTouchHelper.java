package com.sealstudios.Utils;

import android.view.View;

public class ItemTouchHelper {

    public interface OnItemTouchListener {
        void onCardClick(View view, int position);

        void onCardLongClick(View view, int position);
    }

}

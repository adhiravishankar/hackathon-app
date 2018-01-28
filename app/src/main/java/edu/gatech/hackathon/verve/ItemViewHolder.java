package edu.gatech.hackathon.verve;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    ItemViewHolder(ItemView itemView) {
        super(itemView);
    }

    ItemView getItemView() {
        return (ItemView) itemView;
    }
}

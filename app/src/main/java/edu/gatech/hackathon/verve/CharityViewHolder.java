package edu.gatech.hackathon.verve;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CharityViewHolder extends RecyclerView.ViewHolder{

    CharityViewHolder(CharityView itemView) {
        super(itemView);
    }

    CharityView getItemView() {
        return (CharityView) itemView;
    }
}

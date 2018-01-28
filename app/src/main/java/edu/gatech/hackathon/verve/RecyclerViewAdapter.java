package edu.gatech.hackathon.verve;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by adhir on 1/28/2018.
 */

public class RecyclerViewAdapter extends RealmRecyclerViewAdapter<RealmItem, ItemViewHolder> {

    Context context;

    public RecyclerViewAdapter(@Nullable OrderedRealmCollection<RealmItem> data, Context context) {
        super(data, true);
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new ItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ItemView view = holder.getItemView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CharityFinderActivity.class));
            }
        });
        view.bind(getData().get(position));
    }

    public void deleteItem() {
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).id;
    }
}

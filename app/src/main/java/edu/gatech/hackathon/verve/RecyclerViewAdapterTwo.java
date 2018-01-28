package edu.gatech.hackathon.verve;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class RecyclerViewAdapterTwo extends RecyclerView.Adapter<CharityViewHolder> {

    Context context;
    List<Charity> charities;

    public RecyclerViewAdapterTwo(List<Charity> data, Context context) {
        this.charities = data;
        this.context = context;
    }

    @Override
    public CharityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CharityView view = new CharityView(parent.getContext());
        return new CharityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharityViewHolder holder, final int position) {
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/?q=" + charities.get(position).lat + "," + charities.get(position).lon;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
        holder.getItemView().bind(charities.get(position));
    }

    @Override
    public int getItemCount() {
        return charities.size();
    }
}

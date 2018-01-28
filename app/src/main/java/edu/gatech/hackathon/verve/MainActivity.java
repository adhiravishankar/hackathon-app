package edu.gatech.hackathon.verve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        RealmItem item1 = new RealmItem("Cheese", 5, 5.00);
        item1.id = System.currentTimeMillis();
        RealmItem item2 = new RealmItem("Milk", 10, 2.50);
        item2.id = System.currentTimeMillis();
        RealmItem item3 = new RealmItem("Candy", 1000, 3.50);
        item3.id = System.currentTimeMillis();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item1);
        realm.copyToRealmOrUpdate(item2);
        realm.copyToRealmOrUpdate(item3);
        realm.commitTransaction();

        recycler = findViewById(R.id.recycler);
        final RecyclerViewAdapter adapter;
        adapter = new RecyclerViewAdapter(realm.where(RealmItem.class).findAll(), this);
        recycler.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                recycler.getContext(),
                mLayoutManager.getOrientation()
        );
        recycler.addItemDecoration(mDividerItemDecoration);

        SwipeToDeleteCallback swipeHandler = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                realm.beginTransaction();
                realm.where(RealmItem.class).equalTo("id", viewHolder.getItemId()).findAll().deleteAllFromRealm();
                adapter.deleteItem();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
        itemTouchHelper.attachToRecyclerView(recycler);

        final Activity activity = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, CameraActivity.class));
            }
        });
    }

}

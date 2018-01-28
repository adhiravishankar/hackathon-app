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

import java.util.List;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CharityFinderActivity extends AppCompatActivity {

    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.61.117.173:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VerveService service = retrofit.create(VerveService.class);
        recycler = findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);
        final Activity activity = this;

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                recycler.getContext(),
                mLayoutManager.getOrientation()
        );
        recycler.addItemDecoration(mDividerItemDecoration);

        service.organizations().enqueue(new Callback<List<Charity>>() {
            @Override
            public void onResponse(Call<List<Charity>> call, Response<List<Charity>> response) {
                RecyclerViewAdapterTwo adapter = new RecyclerViewAdapterTwo(response.body(), activity);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Charity>> call, Throwable t) {

            }
        });
    }

}

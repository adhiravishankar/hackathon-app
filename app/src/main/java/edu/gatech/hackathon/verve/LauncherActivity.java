package edu.gatech.hackathon.verve;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import at.favre.lib.armadillo.Armadillo;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by adhir on 1/27/2018.
 */

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // Initialize Realm (just once per application)
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new MyMigration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);

        Handler handler = new Handler();
        final Activity activity = this;

        SharedPreferences preferences = Armadillo.create(getApplicationContext(), "login")
                .encryptionFingerprint(getApplicationContext())
                .build();

        final boolean has_key = preferences.contains("api_key");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (has_key)
                    startActivity(new Intent(activity, MainActivity.class));
                else
                    startActivity(new Intent(activity, LoginActivity.class));
            }
        }, 3000);
    }

}

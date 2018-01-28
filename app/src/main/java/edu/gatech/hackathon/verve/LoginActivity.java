package edu.gatech.hackathon.verve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.favre.lib.armadillo.Armadillo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    VerveService service;

    private static final int PERMISSION_CODE_CAMERA = 3001;

    // UI references.
    private TextInputEditText mNameView;
    private TextInputEditText mPhoneView;

    private FusedLocationProviderClient mFusedLocationClient;

    private Gson gson;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mNameView = findViewById(R.id.name);

        mPhoneView = findViewById(R.id.phone);
        mPhoneView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        gson = new GsonBuilder().create();

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.61.117.173:5000")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(VerveService.class);
        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.isLocationGranted(this))
            PermissionUtils.checkPermission(this, ACCESS_COARSE_LOCATION, PERMISSION_CODE_CAMERA);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @SuppressLint("MissingPermission")
    private void attemptLogin() {

        // Reset errors.
        mNameView.setError(null);
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        final String name = mNameView.getText().toString();
        final String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_password));
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (PermissionUtils.isLocationGranted(this)) {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                login(location, name, phone);

                            }
                        });
            }
        }
    }

    private void login(Location location, String name, String phone) {
        // Got last known location. In some rare situations this can be null.
        String token = FirebaseInstanceId.getInstance().getToken();
        if (location != null) {
            // Logic to handle location object
            System.out.println(token);
            String json = gson.toJson(new LoginInfo(name, phone, token, Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude())));
            System.out.println(json);
            service.login(json).enqueue(new Callback<Token>() {
                @Override
                public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                    SharedPreferences preferences = Armadillo.create(getApplicationContext(), "login")
                            .encryptionFingerprint(getApplicationContext())
                            .build();
                    preferences.edit().putString("api_key", response.body().token).apply();
                    startActivity(new Intent(activity, MainActivity.class));
                }

                @Override
                public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {

                }
            });
        }
    }
}


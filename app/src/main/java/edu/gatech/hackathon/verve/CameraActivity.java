package edu.gatech.hackathon.verve;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.CameraViewImpl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CameraActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE_STORAGE = 3001;
    private static final int PERMISSION_CODE_CAMERA = 3002;

    CameraView cameraView;
    VerveService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraView = findViewById(R.id.camera_view);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://128.61.117.173:5000")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(VerveService.class);

        FloatingActionButton fab = findViewById(R.id.take_picture_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.takePicture();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.isCameraGranted(this)) {
            cameraView.start();
            setupCameraCallbacks();
        } else
            PermissionUtils.checkPermission(this, CAMERA, PERMISSION_CODE_CAMERA);
    }

    private void setupCameraCallbacks () {
        cameraView.setOnPictureTakenListener(new CameraViewImpl.OnPictureTakenListener() {
            @Override
            public void onPictureTaken(Bitmap bitmap) {
                startSavingPhoto(bitmap);
            }
        });
        cameraView.setOnFocusLockedListener(new CameraViewImpl.OnFocusLockedListener() {
            @Override
            public void onFocusLocked() {
                playShutterAnimation();
            }
        });
        cameraView.setOnTurnCameraFailListener(new CameraViewImpl.OnTurnCameraFailListener() {
            @Override
            public void onTurnCameraFail(Exception e) {
                Toast.makeText(CameraActivity.this, "Switch Camera Failed. Does you device has a front camera?",
                        Toast.LENGTH_SHORT).show();
            }
        });
        cameraView.setOnCameraErrorListener(new CameraViewImpl.OnCameraErrorListener() {
            @Override
            public void onCameraError(Exception e) {
                Toast.makeText(CameraActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playShutterAnimation() {

    }

    private void startSavingPhoto(Bitmap bitmap) {
        System.out.println("Photo taken");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        String filename = "bill_" + Long.toString(System.currentTimeMillis()) + ".jpg";
        MultipartBody.Part body = MultipartBody.Part.createFormData("bill", filename, reqFile);
        service.bill(body).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (Item item : response.body()) {
                    RealmItem realmItem = realm.createObject(RealmItem.class, System.currentTimeMillis());
                    realmItem.setName(item.product);
                    realmItem.setCost(item.cost);
                    realmItem.setDays(item.days);
                }
                realm.commitTransaction();
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE:
            case PERMISSION_CODE_CAMERA:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        if (requestCode != PERMISSION_CODE_STORAGE && requestCode != PERMISSION_CODE_CAMERA) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }
}

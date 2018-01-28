package edu.gatech.hackathon.verve;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class PermissionUtils {

    static void checkPermission(Activity activity, String permissionString, int permissionCode) {
        int existingPermissionStatus = checkSelfPermission(activity, permissionString);
        if (existingPermissionStatus == PERMISSION_GRANTED) return;
        ActivityCompat.requestPermissions(activity, new String[]{permissionString}, permissionCode);
    }

    static boolean isLocationGranted(Context context) {
        int storagePermissionGranted = checkSelfPermission(context, ACCESS_COARSE_LOCATION);
        return storagePermissionGranted == PERMISSION_GRANTED;
    }

    static boolean isCameraGranted(Context context) {
        int cameraPermissionGranted = checkSelfPermission(context, CAMERA);
        return cameraPermissionGranted == PERMISSION_GRANTED;
    }
}
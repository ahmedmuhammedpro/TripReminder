package com.example.tripreminder.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tripreminder.R;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class RequestPermissions {
    private static RequestPermissions permissionsObject = null;
    private WeakReference<Activity> context;
    //permissions before adding trip
    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int MAX_NUMBER_REQUEST_PERMISSIONS = 2;
    private static int locationReqCount = 0 ,overlayReqCount=0;
    private static final List<String> sPermissions = Arrays.asList(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    );
    private  RequestPermissions(){
    }

    public static RequestPermissions getInstance(Activity context){
        if(permissionsObject == null)
            permissionsObject = new RequestPermissions();
        permissionsObject.context = new WeakReference<>(context);
        return permissionsObject;
    }
    // check all permissions
    public boolean requestLocPermissionIfNecessary() {
        boolean permStatus = false;
        if(!checkLocationPermissions()){
            if(locationReqCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                locationReqCount += 1;
                ActivityCompat.requestPermissions(
                        context.get(),
                        sPermissions.toArray(new String[0]),
                        REQUEST_CODE_PERMISSIONS);
            }else {
                Toast.makeText(context.get(), "Go to setting to allow access location!", Toast.LENGTH_LONG).show();
                permStatus = false;
                locationReqCount = 0;
                requestOverlayPermissionIfNecessary();
            }
        }else{
            permStatus = true;
            requestOverlayPermissionIfNecessary();
        }
        return permStatus;
    }
    public boolean requestOverlayPermissionIfNecessary() {
        boolean permStatus = false;
        if   (!checkOverlayPermissions()) {
            if (overlayReqCount < 1) {
                overlayReqCount += 1;
                Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.get().getPackageName()));
                context.get().startActivityForResult(permissionIntent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            } else {
                overlayReqCount=0;
                if(checkLocationPermissions()){
                    Toast.makeText(context.get(), "Go to setting to allow display over other apps!", Toast.LENGTH_LONG).show();
                    permStatus = false;
                } else{
                    Toast.makeText(context.get(), R.string.set_permissions_in_settings, Toast.LENGTH_LONG).show();
                    permStatus = false;
                }
            }
        }else{
            permStatus = true;
        }
        return permStatus;
    }
    private boolean checkLocationPermissions() {
        boolean hasPermissions = true;
        for (String permission : sPermissions) {
            if (hasPermissions & ContextCompat.checkSelfPermission(
                    context.get(), permission) == PackageManager.PERMISSION_GRANTED)
                hasPermissions = true;
            else hasPermissions = false;
        }
        return hasPermissions;
    }

  public boolean checkOverlayPermissions(){
       //check permission overlay first
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context.get())) {
           //If the draw over permission is not available open the settings screen
           //to grant the permission.
           return false;
       }
       return true;
   }

}

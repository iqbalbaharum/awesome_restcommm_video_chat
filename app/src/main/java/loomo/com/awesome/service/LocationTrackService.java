package loomo.com.awesome.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import loomo.com.awesome.data.AppIntent;

/**
 * Created by MuhammadIqbal on 23/10/2016.
 */

public class LocationTrackService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final static String TAG = "LOCATIONTRACKSERVICE";

    private final static int LOCATION_INTERVAL_NORMAL = 10000; // 10 second
    private final static int LOCATION_INTERVAL_FAST = 1000; // 1 sec

    private boolean mActive;

    private LocationManager mLocationManager;
    private GoogleApiClient mGoogleApiApiClient;
    private LocationRequest mLocationRequest;

    // Single instance
    private IBinder mBinder = new LocationTrackServiceBinder();

    /*************************************** BINDER ********************************************/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocationTrackServiceBinder extends Binder {
        public LocationTrackService getServiceInstance() {
            return LocationTrackService.this;
        }
    }

    /************************************* RUN SERVICE *******************************************/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!mGoogleApiApiClient.isConnected()) {
            mGoogleApiApiClient.connect();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        if(mLocationManager == null) {
            getApplicationContext();
            mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        }

        mGoogleApiApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_INTERVAL_NORMAL)
                .setFastestInterval(LOCATION_INTERVAL_FAST);
    }

    @Override
    public void onDestroy() {

        if(mLocationManager != null) {
            mLocationManager = null;
        }
        super.onDestroy();
    }

    /************************************* LOCATION *******************************************/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(!checkGPSPermission()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiApiClient);
            if(location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "SUSPEND");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "FAILED");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /***
     * Get new location
     * @param location
     */
    private void handleNewLocation(Location location) {

        Intent intent = new Intent();
        intent.setAction(AppIntent.INTENT_USERLOCATION);
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /***
     * User need to authorized to used its GPS service
     * @return
     */
    private boolean checkGPSPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }
}

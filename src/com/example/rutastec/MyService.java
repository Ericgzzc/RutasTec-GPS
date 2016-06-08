package com.example.rutastec;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MyService extends Service implements ConnectionCallbacks,
OnConnectionFailedListener, LocationListener
{
Firebase ref;
Firebase postRef;

String ruta;
TextView nombre;
TextView lat;
TextView lon;
String recorrido;


//LogCat tag
private static final String TAG = RutaActivity.class.getSimpleName();

private Location mLastLocation;

// Google client to interact with Google API
private GoogleApiClient mGoogleApiClient;

// boolean flag to toggle periodic location updates
private boolean mRequestingLocationUpdates = false;

private LocationRequest mLocationRequest;

// Location updates intervals in sec
private static int UPDATE_INTERVAL = 1000; // 5 sec
private static int FATEST_INTERVAL = 800; // 2 sec
private static int DISPLACEMENT = 5; // 10 meters

// UI elements


private final IBinder mBinder = new LocalBinder();


@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	// Building the GoogleApi client

	final int RANDOM = (int) (Math.random()*1000);
	if(intent != null){
		Bundle datos = intent.getExtras(); 
		if(datos != null){
			ruta = datos.getString("ruta");
		}
	}
	Log.e(TAG, "onCreate");
	
	
	Firebase.setAndroidContext(this);
	ref = new Firebase("https://rutastec.firebaseio.com/"+ ruta);
	postRef = ref.child("ruta");
	postRef.setValue("-1");
	postRef = ref.child(""+RANDOM);
	
	mRequestingLocationUpdates = true;
	
	buildGoogleApiClient();
    createLocationRequest();
	
	
    if (mGoogleApiClient != null) {
        mGoogleApiClient.connect();
    }
   
    return START_NOT_STICKY;
}



private void displayLocation() {
	 
    mLastLocation = LocationServices.FusedLocationApi
            .getLastLocation(mGoogleApiClient);

    if (mLastLocation != null) {
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        
        Map<String, Object> location = new HashMap<String, Object>();
        location.put("latitude", "" +latitude);
        location.put("longitude","" + longitude);
        postRef.updateChildren(location);
        
      
            
            RutaActivity.lat.setText("" + latitude);
            RutaActivity.lon.setText("" + longitude);
            //String mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            
      

    } else {
    	
    	Toast.makeText(getApplicationContext(), "Couldn't get the location. Make sure location is enabled on the device",
                Toast.LENGTH_SHORT).show();
    }
}

@Override
public void onDestroy() {
    super.onDestroy();
    if (mGoogleApiClient.isConnected()) {
        mGoogleApiClient.disconnect();
    }
    postRef.removeValue();
}


/**
 * Creating google api client object
 * */
protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build();
}

/**
 * Creating location request object
 * */
protected void createLocationRequest() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FATEST_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
}

/**
 * Method to verify google play services on the device
 * */

/**
 * Starting the location updates
 * */
protected void startLocationUpdates() {

    LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient, mLocationRequest, this);

}

/**
 * Stopping location updates
 */
protected void stopLocationUpdates() {
    LocationServices.FusedLocationApi.removeLocationUpdates(
            mGoogleApiClient, this);
}

/**
 * Google api callback methods
 */
@Override
public void onConnectionFailed(ConnectionResult result) {
    Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
            + result.getErrorCode());
}

@Override
public void onConnected(Bundle arg0) {

    // Once connected with google api, get the location
    displayLocation();

    if (mRequestingLocationUpdates) {
        startLocationUpdates();
    }
}

@Override
public void onConnectionSuspended(int arg0) {
    mGoogleApiClient.connect();
}

@Override
public void onLocationChanged(Location location) {
    // Assign the new location
    mLastLocation = location;

    Toast.makeText(getApplicationContext(), "Location changed!",
            Toast.LENGTH_SHORT).show();

    // Displaying the new location on UI
    displayLocation();
}


/**
 * Class used for the client Binder.  Because we know this service always
 * runs in the same process as its clients, we don't need to deal with IPC.
 */
public class LocalBinder extends Binder {
	MyService getService() {
        // Return this instance of LocalService so clients can call public methods
        return MyService.this;
    }
}

@Override
public boolean onUnbind(Intent intent) {
    stopSelf();
    return super.onUnbind(intent);
}

@Override
public IBinder onBind(Intent intent) {
    return mBinder;
}

/** method for clients */
public String getRuta() {
	return ruta;
}
}
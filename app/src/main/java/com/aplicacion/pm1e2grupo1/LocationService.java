package com.aplicacion.pm1e2grupo1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

public class LocationService extends Service {

    private LocationListener Locatlistener;
    private LocationManager ManagerLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Locatlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent l = new Intent("location_update");
                l.putExtra("longitud",location.getLongitude());
                l.putExtra("latitud", location.getLatitude());
                sendBroadcast(l);
            }

            @Override
            public void onStatusChanged(String s, int l, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent l = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(l);
            }
        };

        ManagerLocation = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        ManagerLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0,Locatlistener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ManagerLocation != null){
            //noinspection MissingPermission
            ManagerLocation.removeUpdates(Locatlistener);
        }
    }

}

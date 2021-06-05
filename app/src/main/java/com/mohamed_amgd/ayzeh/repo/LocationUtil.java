package com.mohamed_amgd.ayzeh.repo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationUtil {
    private static LocationUtil mInstance;
    private MutableLiveData<UserLocation> locationLiveData;
    private FusedLocationProviderClient mFusedLocationClient;


    private LocationUtil(FusedLocationProviderClient fusedLocationClient) {
        locationLiveData = new MutableLiveData<>();
        mFusedLocationClient = fusedLocationClient;
        askForLocationUpdate();
        getLastLocation();
    }

    public static LocationUtil getInstance(FusedLocationProviderClient fusedLocationClient) {
        if (mInstance == null) {
            mInstance = new LocationUtil(fusedLocationClient);
        }
        return mInstance;
    }

    public MutableLiveData<UserLocation> getLocationLiveData() {
        return locationLiveData;
    }

    @SuppressLint("MissingPermission")
    public void askForLocationUpdate() {
        if (hasLocationAccess()) {
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            updateLocationLiveData(location);
                        }
                    }
                }
            };
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                updateLocationLiveData(location);
            }
        });
    }

    private void updateLocationLiveData(Location location) {
        if (locationLiveData.getValue() != null) {
            double userLat = locationLiveData.getValue().getLat();
            double userLon = locationLiveData.getValue().getLon();
            boolean userLocationChanged = Util.getInstance()
                    .userLocationChanged(userLat, userLon, location.getLatitude(), location.getLongitude());
            if (userLocationChanged) {
                locationLiveData.setValue(new UserLocation(location.getLatitude(), location.getLongitude()));
            }
        } else {
            locationLiveData.setValue(new UserLocation(location.getLatitude(), location.getLongitude()));
        }
    }

    public boolean hasLocationAccess() {
        return EasyPermissions.hasPermissions(mFusedLocationClient.getApplicationContext()
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static class UserLocation {
        private double lat;
        private double lon;

        public UserLocation(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }

}

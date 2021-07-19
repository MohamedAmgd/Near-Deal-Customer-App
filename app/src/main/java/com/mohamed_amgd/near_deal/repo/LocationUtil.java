package com.mohamed_amgd.near_deal.repo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import pub.devrel.easypermissions.EasyPermissions;

public class LocationUtil {
    private static LocationUtil mInstance;
    public final MutableLiveData<Boolean> needLocationPermissionLiveData;
    public final MutableLiveData<Boolean> needLocationEnabledLiveData;
    private final MutableLiveData<UserLocation> locationLiveData;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;

    private LocationUtil() {
        locationLiveData = new MutableLiveData<>();
        needLocationPermissionLiveData = new MutableLiveData<>();
        needLocationPermissionLiveData.setValue(true);
        needLocationEnabledLiveData = new MutableLiveData<>();
        needLocationEnabledLiveData.setValue(true);
    }

    public static LocationUtil getInstance() {
        if (mInstance == null) {
            mInstance = new LocationUtil();
        }
        return mInstance;
    }

    public void setFusedLocationClient(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.mLocationManager = locationManager;
    }

    public MutableLiveData<UserLocation> getLocationLiveData() {
        askForLocationUpdate();
        getLastLocation();
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
        boolean a = hasLocationPermission();
        boolean b = isLocationEnabled();
        if (hasLocationPermission()) {
            needLocationPermissionLiveData.setValue(false);
        } else {
            needLocationPermissionLiveData.setValue(true);
        }
        if (isLocationEnabled()) {
            needLocationEnabledLiveData.setValue(false);
        } else {
            needLocationEnabledLiveData.setValue(true);
        }
        return hasLocationPermission() && isLocationEnabled();
    }

    public boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(mFusedLocationClient.getApplicationContext()
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // method to check
    // if location is enabled
    public boolean isLocationEnabled() {
        if (mLocationManager != null) {
            return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
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

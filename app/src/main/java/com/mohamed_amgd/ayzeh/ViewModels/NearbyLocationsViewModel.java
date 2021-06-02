package com.mohamed_amgd.ayzeh.ViewModels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mohamed_amgd.ayzeh.Models.Shop;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Fragments.ShopInfoFragment;
import com.mohamed_amgd.ayzeh.repo.Repository;
import com.mohamed_amgd.ayzeh.repo.RepositoryResult;
import com.mohamed_amgd.ayzeh.repo.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class NearbyLocationsViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Shop>> mShops;
    private Observer<ArrayList<Shop>> mShopsObserver;
    private FragmentManager mFragmentManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private WeakReference<Context> mMapContextWeakReference;
    private GoogleMap mMap;
    private double userLat;
    private double userLon;

    @SuppressLint("MissingPermission")
    public NearbyLocationsViewModel(Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        mShops = new MutableLiveData<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication());

        if (hasLocationAccess()) {
            askForLocationUpdate();
            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    updateMapData(location);
                }
            });
        }
    }

    private void updateMapData(Location location) {
        boolean userLocationChanged = Util.getInstance()
                .userLocationChanged(userLat, userLon, location.getLatitude(), location.getLongitude());
        if (userLocationChanged) {
            userLat = location.getLatitude();
            userLon = location.getLongitude();
            focusMapOnUserLocation();
            initShopsLiveData(null);
        }
    }

    @SuppressLint("MissingPermission")
    private void askForLocationUpdate() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        updateMapData(location);
                    }
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    public void onMapReady(GoogleMap googleMap, Context context) {
        mMap = googleMap;
        mMapContextWeakReference = new WeakReference<>(context);
    }

    public void searchViewAction(String query) {
        initShopsLiveData(query);
    }

    private void initShopsLiveData(String query) {
        RepositoryResult<ArrayList<Shop>> result;
        if (query == null) {
            result = Repository.getInstance().getNearbyShops(userLat, userLon);
        } else {
            result = Repository.getInstance().getNearbyShops(userLat, userLon, query);
        }
        result.getIsLoadingLiveData().observeForever(isLoading -> {
            if (result.isFinishedSuccessfully()) {
                mShops.setValue(result.getData().getValue());
            } else if (result.isFinishedWithError()) {
                // TODO: 6/2/2021 show error ui
                Toast.makeText(getApplication(), "Error :" + result.getErrorCode(), Toast.LENGTH_LONG).show();
            } else {
                // TODO: 6/2/2021 show loading ui
                Toast.makeText(getApplication(), "Loading", Toast.LENGTH_LONG).show();
            }
        });
        mMap.clear();
        mShopsObserver = shops -> addShopsMarkers(shops);
        mShops.observeForever(mShopsObserver);
    }

    private boolean hasLocationAccess() {
        return EasyPermissions.hasPermissions(getApplication()
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @SuppressLint("MissingPermission")
    private void focusMapOnUserLocation() {
        mMap.setMyLocationEnabled(true);
        LatLng userLocation = new LatLng(userLat, userLon);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));
    }

    private void addShopsMarkers(ArrayList<Shop> shops) {
        if (shops == null) return;
        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            LatLng shopLocation = new LatLng(shop.getLocationLat(), shop.getLocationLon());
            Marker shopMarker = mMap.addMarker(new MarkerOptions()
                    .position(shopLocation));
            shopMarker.setTag(i);
        }

        mMap.setOnMarkerClickListener(marker -> {
            int shopIndex = (int) marker.getTag();
            shopDialogBuild(mShops.getValue().get(shopIndex));
            return true;
        });
    }

    private void shopDialogBuild(Shop shop) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMapContextWeakReference.get(), R.style.ShopDialog);
        View shopDialogView = LayoutInflater.from(mMapContextWeakReference.get()).inflate(R.layout.selected_shop_dialog, null);
        builder.setView(shopDialogView);
        AlertDialog shopDialog = builder.create();
        WindowManager.LayoutParams layoutParams = shopDialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        shopDialog.getWindow().setAttributes(layoutParams);
        shopDialog.show();
        initShopDialogViews(shopDialog, shop);
    }

    private void initShopDialogViews(AlertDialog shopDialog, Shop shop) {
        ImageView shopImage = shopDialog.findViewById(R.id.shop_image);
        TextView shopName = shopDialog.findViewById(R.id.shop_name_text_view);
        TextView shopDescription = shopDialog.findViewById(R.id.shop_description_text_view);
        TextView shopDistance = shopDialog.findViewById(R.id.distance_text_view);
        TextView details = shopDialog.findViewById(R.id.details_text_view);
        Glide.with(shopDialog.getContext()).load(shop.getImageUrl()).into(shopImage);
        shopName.setText(shop.getName());
        shopDescription.setText(shop.getDescription());
        shopDistance.setText(shop.getDistanceToUser());
        details.setOnClickListener(v -> {
            shopDialog.dismiss();
            // go to shop fragment is delayed by 0.25 second
            // this workaround to fix map become extremely laggy
            // this issue happens because shop dialog takes time to fully dismiss
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Bundle bundle = new Bundle();
                bundle.putString(ShopInfoFragment.SHOP_ID_BUNDLE_TAG, shop.getId());
                ShopInfoFragment shopInfoFragment = new ShopInfoFragment();
                shopInfoFragment.setArguments(bundle);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_layout, shopInfoFragment);
                transaction.addToBackStack(ShopInfoFragment.CLASS_NAME);
                transaction.commit();
            }, 250);
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mShops.removeObserver(mShopsObserver);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        @NonNull
        private final FragmentManager mFragmentManager;


        public Factory(@NonNull Application application, @NonNull FragmentManager fragmentManager) {
            mApplication = application;
            mFragmentManager = fragmentManager;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new NearbyLocationsViewModel(mApplication, mFragmentManager);
        }
    }
}
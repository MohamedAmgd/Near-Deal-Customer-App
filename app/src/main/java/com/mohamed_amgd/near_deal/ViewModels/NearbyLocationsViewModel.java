package com.mohamed_amgd.near_deal.ViewModels;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mohamed_amgd.near_deal.Models.Shop;
import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.Views.Fragments.ShopInfoFragment;
import com.mohamed_amgd.near_deal.repo.ErrorHandler;
import com.mohamed_amgd.near_deal.repo.LocationUtil;
import com.mohamed_amgd.near_deal.repo.Repository;
import com.mohamed_amgd.near_deal.repo.RepositoryResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class NearbyLocationsViewModel extends AndroidViewModel {
    public MutableLiveData<ErrorHandler.Error> mError;
    private FragmentManager mFragmentManager;
    private WeakReference<Context> mMapContextWeakReference;
    private GoogleMap mMap;
    MutableLiveData<LocationUtil.UserLocation> mUserLocationLiveData;

    @SuppressLint("MissingPermission")
    public NearbyLocationsViewModel(Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        mError = new MutableLiveData<>();
        LocationUtil locationUtil = LocationUtil.getInstance();
        mUserLocationLiveData = locationUtil.getLocationLiveData();
    }

    private void updateMapData(LocationUtil.UserLocation location) {
        focusMapOnUserLocation(location);
        initShopsLiveData(null, location);
    }

    public void onMapReady(GoogleMap googleMap, Context context) {
        mMap = googleMap;
        mMapContextWeakReference = new WeakReference<>(context);
        mUserLocationLiveData.observeForever(userLocation -> {
            updateMapData(userLocation);
        });
    }

    public void searchViewAction(String query) {
        initShopsLiveData(query, mUserLocationLiveData.getValue());
    }

    private void initShopsLiveData(String query, LocationUtil.UserLocation location) {
        RepositoryResult<ArrayList<Shop>> result;
        if (query == null) {
            result = Repository.getInstance().getNearbyShops(location);
        } else {
            result = Repository.getInstance().getNearbyShops(location, query);
        }
        result.getIsLoadingLiveData().observeForever(isLoading -> {
            if (result.isFinishedSuccessfully()) {
                addShopsMarkers(result.getData().getValue());
            } else if (result.isFinishedWithError()) {
                mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                        , v -> {
                    initShopsLiveData(query, location);
                }));
            } else {
                // TODO: 6/2/2021 show loading ui
                Toast.makeText(getApplication(), "Loading", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void focusMapOnUserLocation(LocationUtil.UserLocation location) {
        mMap.setMyLocationEnabled(true);
        LatLng userLocation = new LatLng(location.getLat(), location.getLon());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));
    }

    private void addShopsMarkers(ArrayList<Shop> shops) {
        if (shops == null) return;
        mMap.clear();
        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            LatLng shopLocation = new LatLng(shop.getLocationLat(), shop.getLocationLon());
            Marker shopMarker = mMap.addMarker(new MarkerOptions()
                    .position(shopLocation));
            shopMarker.setTag(i);
        }

        mMap.setOnMarkerClickListener(marker -> {
            int shopIndex = (int) marker.getTag();
            shopDialogBuild(shops.get(shopIndex));
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

    public void showError(View view, ErrorHandler.Error error) {
        ErrorHandler.getInstance().showError(view, error);
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
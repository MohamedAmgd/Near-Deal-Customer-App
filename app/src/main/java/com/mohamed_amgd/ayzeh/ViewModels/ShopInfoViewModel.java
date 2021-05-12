package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.Models.Shop;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Adapters.ProductsRecyclerAdapter;
import com.mohamed_amgd.ayzeh.Views.Fragments.ProductFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.ShopInfoFragment;
import com.mohamed_amgd.ayzeh.repo.Repository;

import java.util.ArrayList;
import java.util.Locale;

public class ShopInfoViewModel extends AndroidViewModel {
    private String mShopId;
    private MutableLiveData<Shop> mShopLiveData;
    private FragmentManager mFragmentManager;
    private MutableLiveData<ArrayList<Product>> mProductsLiveData;
    private Observer<ArrayList<Product>> mProductsObserver;

    public ShopInfoViewModel(@NonNull Application application, FragmentManager fragmentManager, Bundle bundle) {
        super(application);
        mFragmentManager = fragmentManager;
        mShopId = bundle.getString(ShopInfoFragment.SHOP_ID_BUNDLE_TAG);
        Repository repository = Repository.getInstance();
        mShopLiveData = repository.getShopById(mShopId);
        mProductsLiveData = repository.getShopProductsByShopId(mShopId);
    }

    public MutableLiveData<Shop> getShopLiveData() {
        return mShopLiveData;
    }

    public void initShopImage(ImageView shopImage) {
        Glide.with(getApplication()).load(mShopLiveData.getValue().getImageUrl()).into(shopImage);
    }

    public void initShopName(TextView shopName) {
        shopName.setText(mShopLiveData.getValue().getName());
    }

    public void getDirectionsAction(View view) {
        double lat = mShopLiveData.getValue().getLocationLat();
        double lon = mShopLiveData.getValue().getLocationLon();
        String uri = String.format(Locale.ENGLISH,"google.navigation:q=%f,%f",lat,lon);
        Uri mapIntentUri = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        view.getContext().startActivity(mapIntent);
    }

    public void initShopDescription(TextView shopDescription) {
        shopDescription.setText(mShopLiveData.getValue().getDescription());
    }

    public void initProductsRecycler(RecyclerView productsRecycler) {
        ArrayList<Product> mProducts = new ArrayList<>();
        ProductsRecyclerAdapter adapter =
                new ProductsRecyclerAdapter(getApplication(), mProducts);
        productsRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
        productsRecycler.setLayoutManager(layoutManager);

        mProductsObserver = new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                mProducts.clear();
                mProducts.addAll(products);
                adapter.notifyDataSetChanged();
            }
        };
        mProductsLiveData.observeForever(mProductsObserver);
        adapter.setProductOnClickListener(getProductOnClickListener());
    }

    private ProductsRecyclerAdapter.OnClickListener getProductOnClickListener() {
        return position -> {
            Product product = mProductsLiveData.getValue().get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ProductFragment.PRODUCT_BUNDLE_TAG, product);
            ProductFragment productFragment = new ProductFragment();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_layout, productFragment);
            transaction.addToBackStack(ProductFragment.CLASS_NAME);
            transaction.commit();
        };
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mProductsLiveData.removeObserver(mProductsObserver);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        @NonNull
        private final FragmentManager mFragmentManager;

        private final Bundle mBundle;

        public Factory(@NonNull Application application, @NonNull FragmentManager fragmentManager, Bundle bundle) {
            mApplication = application;
            mFragmentManager = fragmentManager;
            mBundle = bundle;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ShopInfoViewModel(mApplication, mFragmentManager, mBundle);
        }
    }
}
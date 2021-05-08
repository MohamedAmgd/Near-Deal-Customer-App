package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;
import android.os.Bundle;
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
import com.mohamed_amgd.ayzeh.Models.Offer;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Adapters.OffersRecyclerAdapter;
import com.mohamed_amgd.ayzeh.Views.Fragments.ProductFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.ShopInfoFragment;
import com.mohamed_amgd.ayzeh.repo.Repository;

import java.util.ArrayList;

public class ProductViewModel extends AndroidViewModel {
    private FragmentManager mFragmentManager;
    private Product mProduct;
    private MutableLiveData<ArrayList<Offer>> mOffersLiveData;
    private Observer<ArrayList<Offer>> mOffersObserver;

    public ProductViewModel(@NonNull Application application, FragmentManager fragmentManager, Bundle bundle) {
        super(application);
        mFragmentManager =fragmentManager;
        mProduct = (Product) bundle.get(ProductFragment.PRODUCT_BUNDLE_TAG);

        mOffersLiveData = Repository.getInstance().getProductOffers(mProduct.getId());
    }

    public void initProductImage(ImageView productImage) {
        Glide.with(getApplication()).load(mProduct.getImageUrl()).into(productImage);
    }

    public void initProductName(TextView productName) {
        productName.setText(mProduct.getName());
    }

    public void initProductBrand(TextView productBrand) {
        productBrand.setText(mProduct.getBrand());
    }

    public void initProductDescription(TextView productDescription) {
        productDescription.setText(mProduct.getDescription());
    }

    public void initProductPrice(TextView productPrice) {
        productPrice.setText(mProduct.getPrice());
    }
    private OffersRecyclerAdapter.OnClickListener getOfferOnClickListener() {
        return position -> {
            String shopId = mOffersLiveData.getValue().get(position).getShopId();
            Bundle bundle = new Bundle();
            bundle.putString(ShopInfoFragment.SHOP_ID_BUNDLE_TAG,shopId);
            ShopInfoFragment shopInfoFragment = new ShopInfoFragment();
            shopInfoFragment.setArguments(bundle);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_layout, shopInfoFragment);
            transaction.addToBackStack(ShopInfoFragment.CLASS_NAME);
            transaction.commit();
        };
    }

    public void initOffersRecycler(RecyclerView offersRecycler) {
        ArrayList<Offer> mOffers = new ArrayList<>();
        OffersRecyclerAdapter adapter = new OffersRecyclerAdapter(getApplication(),mOffers);
        offersRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
        offersRecycler.setLayoutManager(layoutManager);

        mOffersObserver = new Observer<ArrayList<Offer>>() {
            @Override
            public void onChanged(ArrayList<Offer> offers) {
                mOffers.clear();
                mOffers.addAll(offers);
                adapter.notifyDataSetChanged();
            }
        };
        mOffersLiveData.observeForever(mOffersObserver);
        adapter.setOfferOnClickListener(getOfferOnClickListener());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mOffersLiveData.removeObserver(mOffersObserver);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        @NonNull
        private final FragmentManager mFragmentManager;

        private final Bundle mBundle;

        public Factory(@NonNull Application application,@NonNull FragmentManager fragmentManager, Bundle bundle) {
            mApplication = application;
            mFragmentManager = fragmentManager;
            mBundle = bundle;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ProductViewModel(mApplication, mFragmentManager, mBundle);
        }
    }

}
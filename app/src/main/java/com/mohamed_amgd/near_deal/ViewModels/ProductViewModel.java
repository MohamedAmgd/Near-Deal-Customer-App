package com.mohamed_amgd.near_deal.ViewModels;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohamed_amgd.near_deal.Models.Offer;
import com.mohamed_amgd.near_deal.Models.Product;
import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.Views.Adapters.OffersRecyclerAdapter;
import com.mohamed_amgd.near_deal.Views.Fragments.ProductFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.ShopInfoFragment;
import com.mohamed_amgd.near_deal.repo.ErrorHandler;
import com.mohamed_amgd.near_deal.repo.Repository;
import com.mohamed_amgd.near_deal.repo.RepositoryResult;

import java.util.ArrayList;

public class ProductViewModel extends AndroidViewModel {
    public MutableLiveData<ErrorHandler.Error> mError;
    public MutableLiveData<Product> mProductLiveData;
    private FragmentManager mFragmentManager;
    private String mProductId;
    private Product mProduct;
    private MutableLiveData<ArrayList<Offer>> mOffersLiveData;
    private Observer<ArrayList<Offer>> mOffersObserver;

    public ProductViewModel(@NonNull Application application, FragmentManager fragmentManager, Bundle bundle) {
        super(application);
        mFragmentManager = fragmentManager;
        mError = new MutableLiveData<>();
        mProductLiveData = new MutableLiveData<>();
        mProductId = (String) bundle.get(ProductFragment.PRODUCT_ID_TAG);
        mProduct = (Product) bundle.get(ProductFragment.PRODUCT_TAG);
        mOffersLiveData = new MutableLiveData<>();
        if (mProductId != null) {
            initProductLiveData();
            initOffersLiveData();
        }
    }

    private void initProductLiveData() {
        if (mProduct != null) {
            mProductLiveData.setValue(mProduct);
        } else if (mProductId != null) {
            RepositoryResult<Product> result = Repository.getInstance().getProduct(mProductId);
            result.getIsLoadingLiveData().observeForever(aBoolean -> {
                if (result.isFinishedSuccessfully()) {
                    mProductLiveData.setValue(result.getData().getValue());
                } else if (result.isFinishedWithError()) {
                    mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                            , v -> {
                        initOffersLiveData();
                    }));
                } else {
                    // TODO: 6/2/2021 show loading ui
                    Toast.makeText(getApplication(), "Loading", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initOffersLiveData() {
        if (mProductId != null) {
            RepositoryResult<ArrayList<Offer>> result
                    = Repository.getInstance().getProductOffers(mProductId);
            result.getIsLoadingLiveData().observeForever(aBoolean -> {
                if (result.isFinishedSuccessfully()) {
                    mOffersLiveData.setValue(result.getData().getValue());
                    if (mOffersLiveData.getValue() == null) return;
                    for (Offer offer :
                            mOffersLiveData.getValue()) {
                        if (offer.getAmount() != 0) {
                            offer.setPriceAsString(offer.getPrice() + "");
                        } else {
                            String outOfStock = getApplication().getString(R.string.out_of_stock);
                            offer.setPriceAsString(outOfStock);
                        }
                    }
                } else if (result.isFinishedWithError()) {
                    mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                            , v -> {
                        initOffersLiveData();
                    }));
                } else {
                    // TODO: 6/2/2021 show loading ui
                    Toast.makeText(getApplication(), "Loading", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void initProductImage(ImageView productImage) {
        Glide.with(getApplication()).load(mProductLiveData.getValue().getImageUrl()).into(productImage);
    }

    public void initProductName(TextView productName) {
        productName.setText(mProductLiveData.getValue().getName());
    }

    public void initProductBrand(TextView productBrand) {
        productBrand.setText(mProductLiveData.getValue().getBrand());
    }

    public void initProductDescription(TextView productDescription) {
        productDescription.setText(mProductLiveData.getValue().getDescription());
    }

    public void initProductPrice(TextView productPrice) {
        String price = mProductLiveData.getValue().getPrice() + "";
        productPrice.setText(price);
    }

    private OffersRecyclerAdapter.OnClickListener getOfferOnClickListener() {
        return position -> {
            String shopId = mOffersLiveData.getValue().get(position).getShopId();
            Bundle bundle = new Bundle();
            bundle.putString(ShopInfoFragment.SHOP_ID_BUNDLE_TAG, shopId);
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
        OffersRecyclerAdapter adapter = new OffersRecyclerAdapter(getApplication(), mOffers);
        offersRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
        offersRecycler.setLayoutManager(layoutManager);

        mOffersObserver = offers -> {
            mOffers.clear();
            mOffers.addAll(offers);
            adapter.notifyDataSetChanged();
        };
        mOffersLiveData.observeForever(mOffersObserver);
        adapter.setOfferOnClickListener(getOfferOnClickListener());
    }

    public void showError(View view, ErrorHandler.Error error) {
        ErrorHandler.getInstance().showError(view, error);
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

        public Factory(@NonNull Application application, @NonNull FragmentManager fragmentManager, Bundle bundle) {
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
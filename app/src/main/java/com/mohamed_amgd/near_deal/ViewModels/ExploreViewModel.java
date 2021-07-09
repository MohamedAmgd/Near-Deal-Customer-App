package com.mohamed_amgd.near_deal.ViewModels;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
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

import com.mohamed_amgd.near_deal.Models.Category;
import com.mohamed_amgd.near_deal.Models.Filter;
import com.mohamed_amgd.near_deal.Models.Product;
import com.mohamed_amgd.near_deal.Models.SearchResult;
import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.Views.Adapters.CategoriesRecyclerAdapter;
import com.mohamed_amgd.near_deal.Views.Adapters.HotDealsRecyclerAdapter;
import com.mohamed_amgd.near_deal.Views.Fragments.HotDealsFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.ProductFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.SearchFragment;
import com.mohamed_amgd.near_deal.repo.ErrorHandler;
import com.mohamed_amgd.near_deal.repo.LocationUtil;
import com.mohamed_amgd.near_deal.repo.Repository;
import com.mohamed_amgd.near_deal.repo.RepositoryResult;

import java.util.ArrayList;

public class ExploreViewModel extends AndroidViewModel {

    private final ArrayList<Category> categories;
    public MutableLiveData<ErrorHandler.Error> mError;
    private FragmentManager mFragmentManager;
    private MutableLiveData<ArrayList<Product>> mHotDealsLiveData;
    private Observer<ArrayList<Product>> mHotDealsObserver;

    ExploreViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        mError = new MutableLiveData<>();
        // init categories
        categories = new ArrayList<>();
        categories.add(new Category(Filter.MEN_CATEGORY, R.drawable.ic_men_category));
        categories.add(new Category(Filter.WOMEN_CATEGORY, R.drawable.ic_women_category));
        categories.add(new Category(Filter.DEVICES_CATEGORY, R.drawable.ic_devices_category));
        categories.add(new Category(Filter.GADGETS_CATEGORY, R.drawable.ic_gadgets_category));
        categories.add(new Category(Filter.TOOLS_CATEGORY, R.drawable.ic_tools_category));

        // init hot deals live data
        mHotDealsLiveData = new MutableLiveData<>();

        LocationUtil locationUtil = LocationUtil.getInstance();
        locationUtil.getLocationLiveData().observeForever(location -> {
            initHotDealsLiveData(location);
        });
        if (!locationUtil.hasLocationAccess()) {
            initHotDealsLiveData(null);
        }
    }

    private void initHotDealsLiveData(LocationUtil.UserLocation location) {
        if (location == null) return;
        RepositoryResult<SearchResult> result
                = Repository.getInstance().getHotDeals(location);
        result.getIsLoadingLiveData().observeForever(aBoolean -> {
            if (result.isFinishedSuccessfully()) {
                SearchResult searchResult = result.getData().getValue();
                mHotDealsLiveData.setValue(searchResult.getResults());
            } else if (result.isFinishedWithError()) {
                mError.setValue(new ErrorHandler.Error(result.getErrorCode()
                        , v -> {
                    initHotDealsLiveData(location);
                }));
            } else {
                // TODO: 6/2/2021 show loading ui
                Toast.makeText(getApplication(), "Loading", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void searchViewAction(String query) {
        Bundle bundle = new Bundle();
        bundle.putString(SearchFragment.QUERY_BUNDLE_TAG, query);
        bundle.putSerializable(SearchFragment.FILTER_BUNDLE_TAG, new Filter());
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, searchFragment);
        transaction.addToBackStack(SearchFragment.CLASS_NAME);
        transaction.commit();
    }

    public void seeAllAction() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchFragment.FILTER_BUNDLE_TAG, new Filter());
        HotDealsFragment hotDealsFragment = new HotDealsFragment();
        hotDealsFragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, hotDealsFragment);
        transaction.addToBackStack(HotDealsFragment.CLASS_NAME);
        transaction.commit();
    }

    public CategoriesRecyclerAdapter.OnClickListener getCategoryOnClickListener() {
        return position -> {
            String categoryName = categories.get(position).getName();
            Bundle bundle = new Bundle();
            Filter filter = new Filter();
            filter.setCategoryName(categoryName);
            bundle.putSerializable(SearchFragment.FILTER_BUNDLE_TAG, filter);
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_layout, searchFragment);
            transaction.addToBackStack(SearchFragment.CLASS_NAME);
            transaction.commit();
        };
    }

    public HotDealsRecyclerAdapter.OnClickListener getHotDealsOnClickListener() {
        return position -> {
            Product product = mHotDealsLiveData.getValue().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(ProductFragment.PRODUCT_ID_TAG, product.getId());
            bundle.putSerializable(ProductFragment.PRODUCT_TAG, product);
            ProductFragment productFragment = new ProductFragment();
            productFragment.setArguments(bundle);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_layout, productFragment);
            transaction.addToBackStack(ProductFragment.CLASS_NAME);
            transaction.commit();
        };
    }

    public void initCategoriesRecycler(RecyclerView categoriesRecycler) {
        CategoriesRecyclerAdapter categoriesRecyclerAdapter =
                new CategoriesRecyclerAdapter(getApplication(), categories);
        categoriesRecycler.setAdapter(categoriesRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoriesRecycler.setLayoutManager(linearLayoutManager);
        categoriesRecyclerAdapter.setCategoryOnClickListener(getCategoryOnClickListener());
    }

    public void initHotDealsRecycler(RecyclerView hotDealsRecycler) {
        ArrayList<Product> mHotDeals = new ArrayList<>();
        HotDealsRecyclerAdapter hotDealsRecyclerAdapter =
                new HotDealsRecyclerAdapter(getApplication(), mHotDeals);
        hotDealsRecycler.setAdapter(hotDealsRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        hotDealsRecycler.setLayoutManager(linearLayoutManager);
        mHotDealsObserver = new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> hotDeals) {
                mHotDeals.clear();
                mHotDeals.addAll(hotDeals);
                hotDealsRecyclerAdapter.notifyDataSetChanged();
            }
        };
        mHotDealsLiveData.observeForever(mHotDealsObserver);
        hotDealsRecyclerAdapter.setHotDealOnClickListener(getHotDealsOnClickListener());
    }

    public void showError(View view, ErrorHandler.Error error) {
        ErrorHandler.getInstance().showError(view, error);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mHotDealsLiveData.removeObserver(mHotDealsObserver);
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
            return (T) new ExploreViewModel(mApplication, mFragmentManager);
        }
    }
}
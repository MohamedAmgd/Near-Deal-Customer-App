package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;
import android.os.Bundle;

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

import com.mohamed_amgd.ayzeh.Models.Category;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Adapters.CategoriesRecyclerAdapter;
import com.mohamed_amgd.ayzeh.Views.Adapters.HotDealsRecyclerAdapter;
import com.mohamed_amgd.ayzeh.Views.Fragments.HotDealsFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.ProductFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.SearchFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.UserInfoFragment;
import com.mohamed_amgd.ayzeh.repo.Repository;

import java.util.ArrayList;

public class ExploreViewModel extends AndroidViewModel {

    private final ArrayList<Category> categories;
    private FragmentManager mFragmentManager;
    private MutableLiveData<ArrayList<Product>> mHotDealsLiveData;
    private Observer<ArrayList<Product>> mHotDealsObserver;

    ExploreViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        mFragmentManager = fragmentManager;
        // init categories
        String[] categoriesNames = getApplication().getResources()
                .getStringArray(R.array.categories_array);
        categories = new ArrayList<>();
        categories.add(new Category(categoriesNames[0], R.drawable.ic_men_category));
        categories.add(new Category(categoriesNames[1], R.drawable.ic_women_category));
        categories.add(new Category(categoriesNames[2], R.drawable.ic_devices_category));
        categories.add(new Category(categoriesNames[3], R.drawable.ic_gadgets_category));
        categories.add(new Category(categoriesNames[4], R.drawable.ic_tools_category));

        // init hot deals live data
        mHotDealsLiveData = Repository.getInstance().getHotDeals();
    }

    public void searchViewAction(String query) {
        Bundle bundle = new Bundle();
        bundle.putString(SearchFragment.QUERY_BUNDLE_TAG, query);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, searchFragment);
        transaction.addToBackStack(SearchFragment.CLASS_NAME);
        transaction.commit();
    }

    public void seeAllAction() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, new HotDealsFragment());
        transaction.addToBackStack(HotDealsFragment.CLASS_NAME);
        transaction.commit();
    }

    public CategoriesRecyclerAdapter.OnClickListener getCategoryOnClickListener() {
        return position -> {
            String categoryName = categories.get(position).getName();
            Bundle bundle = new Bundle();
            bundle.putString(SearchFragment.CATEGORY_FILTER_BUNDLE_TAG, categoryName);
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
            bundle.putSerializable(ProductFragment.PRODUCT_BUNDLE_TAG, product);
            ProductFragment productFragment = new ProductFragment();
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
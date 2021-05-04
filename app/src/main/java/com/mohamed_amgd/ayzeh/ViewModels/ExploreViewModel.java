package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;

public class ExploreViewModel extends AndroidViewModel {

    private final ArrayList<Category> categories;
    private MutableLiveData<ArrayList<Product>> mHotDealsLiveData;
    private Observer<ArrayList<Product>> mHotDealsObserver;

    ExploreViewModel(@NonNull Application application){
        super(application);

        // init categories
        String[] categoriesNames = getApplication().getResources()
                .getStringArray(R.array.categories_array);
        categories = new ArrayList<>();
        categories.add(new Category(categoriesNames[0], R.drawable.ic_men_category));
        categories.add(new Category(categoriesNames[1],R.drawable.ic_women_category));
        categories.add(new Category(categoriesNames[2],R.drawable.ic_devices_category));
        categories.add(new Category(categoriesNames[3],R.drawable.ic_gadgets_category));
        categories.add(new Category(categoriesNames[4],R.drawable.ic_tools_category));

        // init hot deals live data
        // TODO: 5/3/2021 init hot deals live data from repo
        mHotDealsLiveData = new MutableLiveData<>();
    }
    public ArrayList<Category> getCategories() {
        return categories;
    }

    public MutableLiveData<ArrayList<Product>> getHotDealsLiveData() {
        return mHotDealsLiveData;
    }

    public void searchViewAction(String query){
        // TODO: 4/30/2021 open search fragment with query input
    }

    public void seeAllAction(){
        // TODO: 4/30/2021 open search fragment with hot deals filter
    }

    public CategoriesRecyclerAdapter.OnClickListener getCategoryOnClickListener() {
        // TODO: 4/30/2021 open search fragment with category filter
        return null;
    }

    public HotDealsRecyclerAdapter.OnClickListener getHotDealsOnClickListener() {
        // TODO: 5/3/2021  open product fragment with bundle of product detals
        return null;
    }

    public void initCategoriesRecycler(RecyclerView categoriesRecycler){
        CategoriesRecyclerAdapter categoriesRecyclerAdapter =
                new CategoriesRecyclerAdapter(getApplication(), categories);
        categoriesRecycler.setAdapter(categoriesRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoriesRecycler.setLayoutManager(linearLayoutManager);
        categoriesRecyclerAdapter.setCategoryOnClickListener(getCategoryOnClickListener());
    }

    public void initHotDealsRecycler(RecyclerView hotDealsRecycler){
        ArrayList<Product> mHotDeals = new ArrayList<>();
        HotDealsRecyclerAdapter hotDealsRecyclerAdapter =
                new HotDealsRecyclerAdapter(getApplication(),mHotDeals);
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

        public Factory(@NonNull Application application) {
            mApplication = application;
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ExploreViewModel(mApplication);
        }
    }
}
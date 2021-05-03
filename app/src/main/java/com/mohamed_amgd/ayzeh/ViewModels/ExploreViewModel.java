package com.mohamed_amgd.ayzeh.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mohamed_amgd.ayzeh.Models.Category;
import com.mohamed_amgd.ayzeh.Models.Product;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Adapters.CategoriesRecyclerAdapter;
import com.mohamed_amgd.ayzeh.Views.Adapters.HotDealsRecyclerAdapter;

import java.util.ArrayList;

public class ExploreViewModel extends AndroidViewModel {

    private final ArrayList<Category> categories;
    private MutableLiveData<ArrayList<Product>> mHotDealsLiveData;

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
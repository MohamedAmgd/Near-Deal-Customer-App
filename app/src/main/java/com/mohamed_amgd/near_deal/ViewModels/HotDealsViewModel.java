package com.mohamed_amgd.near_deal.ViewModels;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.mohamed_amgd.near_deal.Models.Filter;
import com.mohamed_amgd.near_deal.Models.Product;
import com.mohamed_amgd.near_deal.Models.SearchResult;
import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.Views.Adapters.HotDealsRecyclerAdapter;
import com.mohamed_amgd.near_deal.Views.Fragments.HotDealsFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.ProductFragment;
import com.mohamed_amgd.near_deal.Views.Fragments.SearchFragment;
import com.mohamed_amgd.near_deal.repo.ErrorHandler;
import com.mohamed_amgd.near_deal.repo.LocationUtil;
import com.mohamed_amgd.near_deal.repo.Repository;
import com.mohamed_amgd.near_deal.repo.RepositoryResult;

import java.util.ArrayList;

public class HotDealsViewModel extends AndroidViewModel {

    public MutableLiveData<ErrorHandler.Error> mError;
    private FragmentManager mFragmentManager;
    private String mQuery;
    private Filter mFilter;
    private MutableLiveData<ArrayList<Product>> mHotDealsLiveData;
    private Observer<ArrayList<Product>> mHotDealsObserver;
    private float mMinSearchResultPrice;
    private float mMaxSearchResultPrice;

    public HotDealsViewModel(@NonNull Application application, FragmentManager fragmentManager, Bundle bundle) {
        super(application);
        mFragmentManager = fragmentManager;
        mError = new MutableLiveData<>();
        mQuery = bundle.getString(HotDealsFragment.QUERY_BUNDLE_TAG);
        mFilter = Filter.createFromAnotherFilter((Filter) bundle.getSerializable(SearchFragment.FILTER_BUNDLE_TAG));
        if (mFilter.getOriginalPriceMin() != Filter.NO_PRICE) {
            mMinSearchResultPrice = mFilter.getOriginalPriceMin();
        } else {
            mMinSearchResultPrice = 0f;
        }
        if (mFilter.getOriginalPriceMax() != Filter.NO_PRICE) {
            mMaxSearchResultPrice = mFilter.getOriginalPriceMax();
        } else {
            mMaxSearchResultPrice = 100f;
        }
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
                = Repository.getInstance().getHotDealsSearchResult(location, mQuery, mFilter);
        result.getIsLoadingLiveData().observeForever(aBoolean -> {
            if (result.isFinishedSuccessfully()) {
                SearchResult searchResult = result.getData().getValue();
                if (searchResult.getResults().size() > 0) {
                    mHotDealsLiveData.setValue(searchResult.getResults());
                    mMinSearchResultPrice = searchResult.getMinPrice();
                    mMaxSearchResultPrice = searchResult.getMaxPrice();
                    mFilter.setOriginalPriceMin(searchResult.getMinPrice());
                    mFilter.setOriginalPriceMax(searchResult.getMaxPrice());
                } else {
                    // TODO: 6/2/2021 no results
                    Toast.makeText(getApplication(), "No results found", Toast.LENGTH_LONG).show();
                }

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

    public void initSearchView(SearchView searchView) {
        searchView.setQuery(mQuery, true);
    }


    public void searchViewAction(String query) {
        initNewHotDealsFragment(query, mFilter);
    }

    private void initNewHotDealsFragment(String query, Filter filter) {
        Bundle bundle = new Bundle();
        bundle.putString(HotDealsFragment.QUERY_BUNDLE_TAG, query);
        bundle.putSerializable(HotDealsFragment.FILTER_BUNDLE_TAG, filter);
        HotDealsFragment hotDealsFragment = new HotDealsFragment();
        hotDealsFragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, hotDealsFragment);
        transaction.addToBackStack(HotDealsFragment.CLASS_NAME);
        transaction.commit();
    }

    public void filtersButtonAction(View view) {
        BottomSheetDialog filterDialog = new BottomSheetDialog(view.getContext(), R.style.FilterDialog);
        View filterLayout =
                LayoutInflater.from(view.getContext()).inflate(R.layout.filters_dialog, null);
        ImageButton exitButton = filterLayout.findViewById(R.id.exit_button);
        ChipGroup chipGroup = filterLayout.findViewById(R.id.chip_group);
        RangeSlider rangeSlider = filterLayout.findViewById(R.id.range_slider);
        Button confirmButton = filterLayout.findViewById(R.id.confirm_button);
        initFiltersDialog(rangeSlider, chipGroup);
        exitButton.setOnClickListener(v -> {
            filterDialog.cancel();
        });
        confirmButton.setOnClickListener(v -> {
            filterDialogConfirmAction(filterDialog, rangeSlider, chipGroup);
        });
        filterDialog.setContentView(filterLayout);
        filterDialog.show();
    }

    private void initFiltersDialog(RangeSlider rangeSlider, ChipGroup chipGroup) {
        rangeSlider.setValueFrom(mMinSearchResultPrice);
        rangeSlider.setValueTo(mMaxSearchResultPrice);
        float leftThumb = mMinSearchResultPrice;
        float rightThumb = mMaxSearchResultPrice;
        if (mFilter.getFilterPriceMin() != Filter.NO_PRICE) {
            leftThumb = mFilter.getFilterPriceMin();
        }
        if (mFilter.getFilterPriceMax() != Filter.NO_PRICE) {
            rightThumb = mFilter.getFilterPriceMax();
        }
        rangeSlider.setValues(leftThumb, rightThumb);
        switch (mFilter.getCategoryName()) {
            case Filter.MEN_CATEGORY:
                chipGroup.check(R.id.men_chip);
                break;
            case Filter.WOMEN_CATEGORY:
                chipGroup.check(R.id.women_chip);
                break;
            case Filter.DEVICES_CATEGORY:
                chipGroup.check(R.id.devices_chip);
                break;
            case Filter.GADGETS_CATEGORY:
                chipGroup.check(R.id.gadgets_chip);
                break;
            case Filter.TOOLS_CATEGORY:
                chipGroup.check(R.id.tools_chip);
                break;
            case Filter.NO_CATEGORY:
                chipGroup.clearCheck();
                break;
        }
    }

    private void filterDialogConfirmAction(BottomSheetDialog filterDialog, RangeSlider rangeSlider, ChipGroup chipGroup) {
        Filter newFilter = Filter.createFromAnotherFilter(mFilter);
        ArrayList<Float> values = (ArrayList<Float>) rangeSlider.getValues();
        float leftThumb = values.get(0);
        float rightThumb = values.get(1);
        if (leftThumb > rangeSlider.getValueFrom()) {
            newFilter.setFilterPriceMin(leftThumb);
        }
        if (rightThumb < rangeSlider.getValueTo()) {
            newFilter.setFilterPriceMax(rightThumb);
        }
        int checkedChipId = chipGroup.getCheckedChipId();
        if (checkedChipId == R.id.men_chip) {
            newFilter.setCategoryName(Filter.MEN_CATEGORY);
        } else if (checkedChipId == R.id.women_chip) {
            newFilter.setCategoryName(Filter.WOMEN_CATEGORY);
        } else if (checkedChipId == R.id.devices_chip) {
            newFilter.setCategoryName(Filter.DEVICES_CATEGORY);
        } else if (checkedChipId == R.id.gadgets_chip) {
            newFilter.setCategoryName(Filter.GADGETS_CATEGORY);
        } else if (checkedChipId == R.id.tools_chip) {
            newFilter.setCategoryName(Filter.TOOLS_CATEGORY);
        } else {
            newFilter.setCategoryName(Filter.NO_CATEGORY);
        }
        filterDialog.dismiss();
        initNewHotDealsFragment(mQuery, newFilter);
    }


    public void initHotDealsRecycler(RecyclerView hotDealsRecycler) {
        ArrayList<Product> mResults = new ArrayList<>();
        HotDealsRecyclerAdapter adapter =
                new HotDealsRecyclerAdapter(getApplication(), mResults);
        hotDealsRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication(), 2);
        hotDealsRecycler.setLayoutManager(layoutManager);

        mHotDealsObserver = hotDeals -> {
            mResults.clear();
            mResults.addAll(hotDeals);
            adapter.notifyDataSetChanged();
        };
        mHotDealsLiveData.observeForever(mHotDealsObserver);
        adapter.setHotDealOnClickListener(getHotDealsOnClickListener());
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
            return (T) new HotDealsViewModel(mApplication, mFragmentManager, mBundle);
        }
    }
}
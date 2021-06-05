package com.mohamed_amgd.ayzeh.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.SearchViewModel;

public class SearchFragment extends Fragment {

    public static final String QUERY_BUNDLE_TAG = "query";
    public static final String FILTER_BUNDLE_TAG = "filter";
    public static final String CLASS_NAME = "SearchFragment";

    private SearchViewModel mViewModel;
    private SearchView mSearchView;
    private ImageButton mFiltersButton;
    private RecyclerView mProductsRecycler;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        SearchViewModel.Factory factory =
                new SearchViewModel.Factory(getActivity().getApplication(),getFragmentManager(),bundle);
        mViewModel = new ViewModelProvider(this,factory).get(SearchViewModel.class);
        mSearchView = view.findViewById(R.id.search_view);
        mFiltersButton = view.findViewById(R.id.filters_button);
        mProductsRecycler = view.findViewById(R.id.products_recycler);

        mViewModel.initSearchView(mSearchView);
        mFiltersButton.setOnClickListener(v -> {
            mViewModel.filtersButtonAction(v);
        });
        mViewModel.initProductsRecycler(mProductsRecycler);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.searchViewAction(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });
    }
}
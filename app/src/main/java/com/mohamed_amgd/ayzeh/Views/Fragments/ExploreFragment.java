package com.mohamed_amgd.ayzeh.Views.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.ExploreViewModel;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private SearchView mSearchView;
    private RecyclerView mCategoriesRecycler;
    private RecyclerView mHotDealsRecycler;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.explore_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExploreViewModel.Factory factory = new ExploreViewModel.Factory(getActivity().getApplication());
        mViewModel = new ViewModelProvider(this,factory).get(ExploreViewModel.class);
        mSearchView = getActivity().findViewById(R.id.search_view);
        mCategoriesRecycler = getActivity().findViewById(R.id.categories_recycler);
        mHotDealsRecycler = getActivity().findViewById(R.id.hot_deals_recycler);

        mSearchView.setOnSearchClickListener(v -> {
            String query = (String) mSearchView.getQuery();
            mViewModel.searchViewAction(query);
        });

        mViewModel.initCategoriesRecycler(mCategoriesRecycler);
        mViewModel.initHotDealsRecycler(mHotDealsRecycler);
    }

}
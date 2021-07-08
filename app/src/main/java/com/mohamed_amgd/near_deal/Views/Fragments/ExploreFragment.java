package com.mohamed_amgd.near_deal.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.ViewModels.ExploreViewModel;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private SearchView mSearchView;
    private TextView mSeeAllTextView;
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
        ExploreViewModel.Factory factory =
                new ExploreViewModel.Factory(getActivity().getApplication(), getFragmentManager());
        mViewModel = new ViewModelProvider(this, factory).get(ExploreViewModel.class);
        mSearchView = view.findViewById(R.id.search_view);
        mSeeAllTextView = view.findViewById(R.id.see_all);
        mCategoriesRecycler = view.findViewById(R.id.categories_recycler);
        mHotDealsRecycler = view.findViewById(R.id.hot_deals_recycler);

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
        mSeeAllTextView.setOnClickListener(v -> {
            mViewModel.seeAllAction();
        });

        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });

        mViewModel.initCategoriesRecycler(mCategoriesRecycler);
        mViewModel.initHotDealsRecycler(mHotDealsRecycler);
    }

}
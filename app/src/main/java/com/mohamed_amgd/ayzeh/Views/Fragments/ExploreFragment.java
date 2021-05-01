package com.mohamed_amgd.ayzeh.Views.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mohamed_amgd.ayzeh.Models.Category;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.ExploreViewModel;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private SearchView mSearchView;
    private RecyclerView mCategoriesRecycler;
    private RecyclerView mHotDealsRecycler;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    private ArrayList<Category> categories;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.explore_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        mSearchView = getActivity().findViewById(R.id.search_view);
        mCategoriesRecycler = getActivity().findViewById(R.id.categories_recycler);
        mHotDealsRecycler = getActivity().findViewById(R.id.hot_deals_recycler);

        mSearchView.setOnSearchClickListener(v -> {
            String query = (String) mSearchView.getQuery();
            mViewModel.searchViewAction(query);
        });

        initCategoriesRecycler();
    }

    private void initCategoriesRecycler(){
        String[] categoriesNames = getResources().getStringArray(R.array.categories_array);

        categories = new ArrayList<>();
        categories.add(new Category(categoriesNames[0],R.drawable.ic_men_category));
        categories.add(new Category(categoriesNames[1],R.drawable.ic_women_category));
        categories.add(new Category(categoriesNames[2],R.drawable.ic_devices_category));
        categories.add(new Category(categoriesNames[3],R.drawable.ic_gadgets_category));
        categories.add(new Category(categoriesNames[4],R.drawable.ic_tools_category));


        CategoriesRecyclerAdapter categoriesRecyclerAdapter = new CategoriesRecyclerAdapter(getContext(),categories);
        mCategoriesRecycler.setAdapter(categoriesRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mCategoriesRecycler.setLayoutManager(linearLayoutManager);
        categoriesRecyclerAdapter.setCategoryOnClickListener(mViewModel.getCategoryOnClickListener());
    }

    private void initHotDealsRecycler(){

    }

}
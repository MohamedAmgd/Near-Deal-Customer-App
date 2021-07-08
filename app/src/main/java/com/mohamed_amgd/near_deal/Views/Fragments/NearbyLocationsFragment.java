package com.mohamed_amgd.near_deal.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.SupportMapFragment;
import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.ViewModels.NearbyLocationsViewModel;

public class NearbyLocationsFragment extends Fragment {

    private NearbyLocationsViewModel mViewModel;
    private SearchView mSearchView;
    private SupportMapFragment mMapFragment;

    public static NearbyLocationsFragment newInstance() {
        return new NearbyLocationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nearby_locations_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NearbyLocationsViewModel.Factory factory = new NearbyLocationsViewModel.Factory(getActivity().getApplication(), getFragmentManager());
        mViewModel = new ViewModelProvider(this, factory).get(NearbyLocationsViewModel.class);

        mSearchView = view.findViewById(R.id.search_view);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

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
        mMapFragment.getMapAsync(googleMap -> {
            mViewModel.onMapReady(googleMap, mMapFragment.getContext());
        });

        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });
    }
}
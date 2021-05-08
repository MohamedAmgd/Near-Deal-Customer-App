package com.mohamed_amgd.ayzeh.Views.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamed_amgd.ayzeh.ViewModels.HotDealsViewModel;
import com.mohamed_amgd.ayzeh.R;

public class HotDealsFragment extends Fragment {

    private HotDealsViewModel mViewModel;

    public static HotDealsFragment newInstance() {
        return new HotDealsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hot_deals_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HotDealsViewModel.class);
        // TODO: Use the ViewModel
    }

}
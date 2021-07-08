package com.mohamed_amgd.near_deal.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamed_amgd.near_deal.R;
import com.mohamed_amgd.near_deal.ViewModels.ProductViewModel;

public class ProductFragment extends Fragment {
    public static final String PRODUCT_ID_TAG = "product_id";
    public static final String CLASS_NAME = "ProductFragment";
    public static final String PRODUCT_TAG = "product";

    private ProductViewModel mViewModel;
    private ImageView mProductImage;
    private TextView mProductName;
    private TextView mProductBrand;
    private TextView mProductDescription;
    private TextView mProductPrice;
    private RecyclerView mOffersRecycler;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        ProductViewModel.Factory factory = new ProductViewModel.Factory(getActivity().getApplication()
                , getFragmentManager(), bundle);

        mViewModel = new ViewModelProvider(this, factory).get(ProductViewModel.class);
        mProductImage = getActivity().findViewById(R.id.product_image);
        mProductName = getActivity().findViewById(R.id.product_name);
        mProductBrand = getActivity().findViewById(R.id.product_brand);
        mProductDescription = getActivity().findViewById(R.id.product_description);
        mProductPrice = getActivity().findViewById(R.id.product_price);
        mOffersRecycler = getActivity().findViewById(R.id.offers_recycler);

        mViewModel.mProductLiveData.observe(getViewLifecycleOwner(), product -> {
            mViewModel.initProductImage(mProductImage);
            mViewModel.initProductName(mProductName);
            mViewModel.initProductBrand(mProductBrand);
            mViewModel.initProductDescription(mProductDescription);
            mViewModel.initProductPrice(mProductPrice);
        });
        mViewModel.initOffersRecycler(mOffersRecycler);
        mViewModel.mError.observe(getViewLifecycleOwner(), error -> {
            mViewModel.showError(view, error);
        });
    }
}
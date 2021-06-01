package com.mohamed_amgd.ayzeh.Views.Fragments;

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

import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.ViewModels.ShopInfoViewModel;

public class ShopInfoFragment extends Fragment {

    public static final String SHOP_ID_BUNDLE_TAG = "shop_id";
    public static final String CLASS_NAME = "ShopInfoFragment";
    private ShopInfoViewModel mViewModel;

    private ImageView mShopImage;
    private TextView mShopName;
    private TextView mGetDirections;
    private TextView mShopDescription;
    private RecyclerView mProductsRecycler;

    public static ShopInfoFragment newInstance() {
        return new ShopInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        ShopInfoViewModel.Factory factory =
                new ShopInfoViewModel.Factory(getActivity().getApplication()
                , getFragmentManager(), bundle);
        mViewModel = new ViewModelProvider(this,factory).get(ShopInfoViewModel.class);

        mShopImage = view.findViewById(R.id.shop_image);
        mShopName = view.findViewById(R.id.shop_name_text_view);
        mGetDirections = view.findViewById(R.id.get_directions_text_view);
        mShopDescription = view.findViewById(R.id.shop_description_text_view);
        mProductsRecycler = view.findViewById(R.id.shop_products_recycler);

        mViewModel.getShopLiveData().observe(getViewLifecycleOwner(),shop -> {
            mViewModel.initShopImage(mShopImage);
            mViewModel.initShopName(mShopName);
            mViewModel.initShopDescription(mShopDescription);
            mViewModel.initProductsRecycler(mProductsRecycler);

            mGetDirections.setOnClickListener(v -> {
                mViewModel.getDirectionsAction(v);
            });
        });
    }
}
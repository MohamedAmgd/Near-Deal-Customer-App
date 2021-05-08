package com.mohamed_amgd.ayzeh.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mohamed_amgd.ayzeh.Views.Fragments.ExploreFragment;
import com.mohamed_amgd.ayzeh.Views.Fragments.NearbyLocationsFragment;
import com.mohamed_amgd.ayzeh.R;
import com.mohamed_amgd.ayzeh.Views.Fragments.UserInfoFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.bottom_nav_view);
        increaseExploreItemSize();
        initNavigation();
    }

    private void increaseExploreItemSize(){
        final int valueInPixels = (int) getResources().getDimension(R.dimen.explore_bottom_nav_item_icon_size);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        final View iconView = menuView.getChildAt(0).findViewById(com.google.android.material.R.id.icon);
        final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        // set your height here
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                ,  valueInPixels, displayMetrics);
        // set your width here
        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , valueInPixels, displayMetrics);
        iconView.setLayoutParams(layoutParams);
    }

    private void initNavigation(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fragment_layout,new ExploreFragment());
        mFragmentTransaction.commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.explore_menu_item) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_layout,new ExploreFragment());
                mFragmentTransaction.commit();
            }  else if (itemId == R.id.nearby_locations_menu_item) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_layout,new NearbyLocationsFragment());
                mFragmentTransaction.commit();
            } else if (itemId == R.id.account_menu_item) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_layout,new UserInfoFragment());
                mFragmentTransaction.commit();
            }
            return true;
        });
    }
}
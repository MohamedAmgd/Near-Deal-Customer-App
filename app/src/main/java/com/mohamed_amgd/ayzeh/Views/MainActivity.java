package com.mohamed_amgd.ayzeh.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
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

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 52;

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
                if(hasLocationAccess()){
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.fragment_layout,new NearbyLocationsFragment());
                    mFragmentTransaction.commit();
                } else {
                    requestLocationPermission();
                    return false;
                }
            } else if (itemId == R.id.account_menu_item) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_layout,new UserInfoFragment());
                mFragmentTransaction.commit();
            }
            return true;
        });
    }

    private boolean hasLocationAccess() {
        return EasyPermissions.hasPermissions(getApplication()
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestLocationPermission(){
        EasyPermissions.requestPermissions(this
                , getString(R.string.location_permission_rationale)
                , LOCATION_PERMISSION_REQUEST_CODE
                , Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        mBottomNavigationView.setSelectedItemId(R.id.nearby_locations_menu_item);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
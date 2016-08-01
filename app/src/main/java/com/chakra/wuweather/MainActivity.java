package com.chakra.wuweather;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.ZipCodeData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WeatherFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String KEY_ZIP_CODE = "key_zip_code";
    private static final String FRAGMENT_TAG = "wether_fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeatherFragment fragment = new WeatherFragment();
        changeFragment(fragment);
    }

    @Override
    public void onListFragmentInteraction(ZipCodeData data) {
        showDetailsFragment(data);
    }

    void showDetailsFragment(ZipCodeData data) {
        DetailsFragment fragment = DetailsFragment.getInstance(data);
        changeFragment(fragment);
    }

    public void changeFragment(android.support.v4.app.Fragment fragment) {

        if (fragment == null) {
            return;
        }
        String backStateName = fragment.getClass().getSimpleName();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, FRAGMENT_TAG);
        ft.addToBackStack(backStateName);
//        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        try {
            ft.commit();
        }
        catch (Exception e)
        {
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment currentFragment = getCurrentFragment();

        Log.v(TAG, "onBackPressed ::" + currentFragment);

        if(null==currentFragment){
            finish();
        }
    }

    private Fragment getCurrentFragment() {
        Fragment mnFragment;
        mnFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return mnFragment;
    }

    /*private String getCurrentZipCode() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder gcoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> Data = null;
        double latitude;
        double longitude;
        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            try {
                Data = gcoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String cityname = Data.get(0).getLocality();
            String postalcode = Data.get(0).getPostalCode();

            String message = String.format("City Name \n PostalCode \n", Data.get(0).getLocality(), Data.get(0).getPostalCode());
            Log.d(TAG, message);
            return postalcode;
        }
        return null;
    }*/


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestoy()");
        super.onDestroy();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String zipCode = getCurrentZipCode();
    }*/
}

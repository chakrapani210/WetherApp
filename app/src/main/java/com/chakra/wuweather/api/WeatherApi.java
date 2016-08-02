package com.chakra.wuweather.api;

import android.app.Application;
import android.content.Context;

import com.chakra.wuweather.vendors.WuWeatherApi;

import java.util.ArrayList;

/**
 * Created by f68dpim on 7/30/16.
 */
public class WeatherApi implements IWeatherApi {
    IWeatherApi mWeatherAoi;
    public enum VendorType {
        WU_API, DARK_SKY_API, OPEN_WEATHER_API
    }
    public WeatherApi(VendorType type, Context context) {
        switch (type) {
            case WU_API:
                mWeatherAoi = new WuWeatherApi(context);
                break;
            default:
                new IllegalArgumentException(type + " is not supported for now");
        }
    }
    @Override
    public void getForecast(String zipCode, OnResultCallback callback) {
        mWeatherAoi.getForecast(zipCode, callback);
    }
}

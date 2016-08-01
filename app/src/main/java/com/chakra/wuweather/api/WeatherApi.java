package com.chakra.wuweather.api;

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

    @Override
    public ArrayList<ZipCodeData> getForecast(ArrayList<String> zipCodes) {
        return mWeatherAoi.getForecast(zipCodes);
    }

    public WeatherApi(VendorType type) {
        switch (type) {
            case WU_API:
                mWeatherAoi = new WuWeatherApi();
                break;
            default:
                new IllegalArgumentException(type + " is not supported for now");
        }
    }

    @Override
    public ZipCodeData getForecast(String zipCode) {
        return mWeatherAoi.getForecast(zipCode);
    }
}

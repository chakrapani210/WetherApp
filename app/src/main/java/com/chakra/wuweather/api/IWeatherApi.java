package com.chakra.wuweather.api;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by f68dpim on 7/30/16.
 */
public interface IWeatherApi {
    public static interface OnResultCallback {
        void onSuccess(ZipCodeData data);
        void onError(Error error);
    }
    void getForecast(String zipCode, OnResultCallback callback);
}

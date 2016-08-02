package com.chakra.wuweather.vendors;

import com.chakra.wuweather.api.IWeatherApi;
import com.chakra.wuweather.api.InvalidZipCodeException;
import com.chakra.wuweather.api.ZipCodeData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f68dpim on 8/1/16.
 */
public interface IZipCodeDataParser {
    ZipCodeData parseData(JSONObject jsonObj, IWeatherApi.OnResultCallback callback) throws JSONException, InvalidZipCodeException;
}

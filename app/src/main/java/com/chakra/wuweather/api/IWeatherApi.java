package com.chakra.wuweather.api;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by f68dpim on 7/30/16.
 */
public interface IWeatherApi {
    ZipCodeData getForecast(String zipCode);
    ArrayList<ZipCodeData> getForecast(ArrayList<String> zipCode);
}

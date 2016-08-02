package com.chakra.wuweather.vendors;

import android.content.Context;
import android.util.Log;

import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.IWeatherApi;
import com.chakra.wuweather.api.InvalidZipCodeException;
import com.chakra.wuweather.api.ZipCodeData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Chakrapani on 4/12/2016.
 */
public class WuWeatherApi extends AbstractWeatherApi {
    private static final String TAG = WuWeatherApi.class.getSimpleName();
    private static final String API_KEY = "fd15326601793e29"; /*hard coded. never mind for now*/
    public static final String BASE_API_URI = "http://api.wunderground.com/api/";
    public static final String QUERY_URI = "/forecast/q/*.json";

    public WuWeatherApi(Context context) {
        super(context);
    }

    @Override
    protected URL getUrl(String zipCode) throws IOException{
        String query = QUERY_URI.replace("*", zipCode);
        Log.d(TAG, "Url: " + BASE_API_URI + API_KEY + query);
        URL url = new URL(BASE_API_URI + API_KEY + query);
        return url;
    }

    public ZipCodeData parseData(JSONObject jsonObj, IWeatherApi.OnResultCallback callback) throws JSONException, InvalidZipCodeException {
        ZipCodeData result = new ZipCodeData();
        ArrayList<ForecastData> data = new ArrayList<ForecastData>();

            JSONObject error = jsonObj.getJSONObject("response").optJSONObject("error");
            if(error != null) {
                throw new InvalidZipCodeException();
            } else {
                JSONArray forecastday = jsonObj.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday");
                for (int i = 0; i < forecastday.length(); i++) {
                    ForecastData day = new ForecastData();
                    JSONObject today = forecastday.getJSONObject(i);
                    StringBuilder date = new StringBuilder("");
                    date.append(today.getJSONObject("date").getString("month"));
                    date.append("/");
                    date.append(today.getJSONObject("date").getString("day"));
                    date.append("/");
                    date.append(today.getJSONObject("date").getString("year"));
                    day.setDate(date.toString());
                    day.setLow(today.getJSONObject("low").getString("fahrenheit"));
                    day.setHigh(today.getJSONObject("high").getString("fahrenheit"));
                    day.setCondition(today.getString("conditions"));
                    data.add(day);
                }
                result.setForecastData(data);
            }

        return result;
    }
}

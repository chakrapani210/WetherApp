package com.chakra.wuweather.vendors;

import android.util.Log;

import com.chakra.wuweather.api.Error;
import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.IWeatherApi;
import com.chakra.wuweather.api.ZipCodeData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Chakrapani Chittabathina on 7/30/16.
 */
public abstract class AbstractWeatherApi implements IWeatherApi {
    private static final String TAG = AbstractWeatherApi.class.getSimpleName();

    public ZipCodeData getForecast(String zipCode) {

        HttpURLConnection conn = null;
        ZipCodeData data = null;
        try {
            URL url = getUrl(zipCode);
            Log.d(TAG, "Url: " + url);
            conn = (HttpURLConnection) url.openConnection();
            data = readForecastData(conn);
            data.setZipCode(zipCode);
        } catch(IOException e) {
            data = new ZipCodeData();
            data.setZipCode(zipCode);
            data.setError(new Error(Error.ErrorCode.INTERNET_UNAVAILABLE));
            Log.e(TAG, "Exception:", e);
        } finally{
            if(conn != null) {
                conn.disconnect();
            }
        }

        return data;
    }

    abstract protected URL getUrl(String zipCode) throws IOException;

    @Override
    public ArrayList<ZipCodeData> getForecast(ArrayList<String> zipCodes) {
        ArrayList<ZipCodeData> data = new ArrayList<ZipCodeData>();
        for (String code : zipCodes) {
            data.add(getForecast(code));
        }
        return data;
    }

    private ZipCodeData readForecastData(HttpURLConnection conn) {
        ZipCodeData error =  new ZipCodeData();
        StringBuilder data = new StringBuilder();
        JSONObject jsonObj = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            Log.d(TAG, result);
            try {
                jsonObj = new JSONObject(result);
            } catch (JSONException e) {
                error.setError(new Error(Error.ErrorCode.DATA_NOT_AVAILABLE));
                return error;
            }
        } catch (IOException e) {
            error.setError(new Error(Error.ErrorCode.INTERNET_UNAVAILABLE));
            return error;
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return parseData(jsonObj);
    }

    protected abstract ZipCodeData parseData(JSONObject jsonObj);
}

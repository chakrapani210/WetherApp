package com.chakra.wuweather.vendors;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
public abstract class AbstractWeatherApi implements IWeatherApi, IZipCodeDataParser {
    private static final String TAG = AbstractWeatherApi.class.getSimpleName();
    private final Context mContext;
    private static RequestQueue sRequestQueue;

    public AbstractWeatherApi(Context context) {
        this.mContext = context;
        if(sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
    }

    @Override
    public void getForecast(String zipCode, OnResultCallback callback) {
        try {
            URL url = getUrl(zipCode);
            Log.d(TAG, "Url: " + url);
            getDataFromVolley(url, callback);
        } catch(IOException e) {
            callback.onError(new Error(Error.ErrorCode.INTERNET_UNAVAILABLE));
            Log.e(TAG, "Exception:", e);
        }
    }

    private void getDataFromVolley(URL url, OnResultCallback callback) {
        ZipCodeDataRequest request = new ZipCodeDataRequest(url.toString(), this, callback);
        sRequestQueue.add(request);
    }

    abstract protected URL getUrl(String zipCode) throws IOException;
}

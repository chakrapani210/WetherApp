package com.chakra.wuweather.vendors;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.chakra.wuweather.api.Error;
import com.chakra.wuweather.api.IWeatherApi;
import com.chakra.wuweather.api.InvalidZipCodeException;
import com.chakra.wuweather.api.ZipCodeData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by f68dpim on 8/1/16.
 */
public class ZipCodeDataRequest extends Request<ZipCodeData> {
    private final IWeatherApi.OnResultCallback mListner;
    private final IZipCodeDataParser mParser;
    private final Gson gson = new Gson();
    private static Error sError;

    public ZipCodeDataRequest(String url, IZipCodeDataParser parser,
                              final IWeatherApi.OnResultCallback callback) {
        super(Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(sError != null) {
                    callback.onError(sError);
                } else {
                    callback.onError(new Error(Error.ErrorCode.DATA_NOT_AVAILABLE));
                }
            }
        });
        mListner = callback;
        mParser = parser;
    }

    @Override
    protected Response<ZipCodeData> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonObj = new JSONObject(json);
            return Response.success(mParser.parseData(jsonObj, mListner),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (InvalidZipCodeException e) {
            sError = new Error(Error.ErrorCode.INVALID_ZIP_CODE);
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ZipCodeData response) {
        mListner.onSuccess(response);
    }
}

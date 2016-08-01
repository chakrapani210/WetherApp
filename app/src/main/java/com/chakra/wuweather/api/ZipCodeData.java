package com.chakra.wuweather.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by f68dpim on 7/31/16.
 */
public class ZipCodeData implements Parcelable {
    private String mZipCode;
    private ArrayList<ForecastData> mForecastData;
    private Error mError;

    public ZipCodeData() {

    }

    public ZipCodeData(Parcel source) {
        mZipCode = source.readString();
        mForecastData = source.readArrayList(ZipCodeData.class.getClassLoader());
        mError = source.readParcelable(Error.class.getClassLoader());
    }


    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        this.mError = error;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        this.mZipCode = zipCode;
    }

    public ArrayList<ForecastData> getForecastData() {
        return mForecastData;
    }

    public void setForecastData(ArrayList<ForecastData> forecastData) {
        this.mForecastData = forecastData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mZipCode);
        dest.writeList(mForecastData);
        dest.writeParcelable(mError, flags);
    }

    public Creator<ZipCodeData> CREATOR = new Creator<ZipCodeData>() {
        @Override
        public ZipCodeData createFromParcel(Parcel source) {
            return new ZipCodeData(source);
        }

        @Override
        public ZipCodeData[] newArray(int size) {
            return new ZipCodeData[0];
        }
    };
}

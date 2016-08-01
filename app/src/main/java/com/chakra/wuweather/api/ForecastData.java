package com.chakra.wuweather.api;

import android.os.Parcel;
import android.os.Parcelable;

public class ForecastData implements Parcelable {
    private String mHigh;
    private String mLow;
    private String mCondition;
    private String mDate;
    public ForecastData() {
    }
    public ForecastData(Parcel source) {
        mHigh = source.readString();
        mLow = source.readString();
        mCondition = source.readString();
        mDate = source.readString();
    }

    public String getLow() {
        return mLow;
    }

    public void setLow(String low) {
        this.mLow = low;
    }

    public String getHigh() {
        return mHigh;
    }

    public void setHigh(String high) {
        this.mHigh = high;
    }

    public String getCondition() {
        return mCondition;
    }

    public void setCondition(String condition) {
        this.mCondition = condition;
    }

    @Override
    public String toString() {
        return "ForecastData{" +
                "mDate='" + mDate + '\'' +
                ", mCondition='" + mCondition + '\'' +
                ", mLow='" + mLow + '\'' +
                ", mHigh='" + mHigh + '\'' +
                '}';
    }

    public void setDate(String date) {
        this.mDate = date;
    }
    public String getDate() {
        return this.mDate;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHigh);
        dest.writeString(mLow);
        dest.writeString(mCondition);
        dest.writeString(mDate);
    }

    public Creator<ForecastData> CREATOR = new Creator<ForecastData>() {
        @Override
        public ForecastData createFromParcel(Parcel source) {
            return new ForecastData(source);
        }

        @Override
        public ForecastData[] newArray(int size) {
            return new ForecastData[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}

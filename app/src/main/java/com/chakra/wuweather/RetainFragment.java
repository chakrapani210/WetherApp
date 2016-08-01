package com.chakra.wuweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.WeatherApi;
import com.chakra.wuweather.api.ZipCodeData;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RetainFragment extends Fragment {

    public static final String RETAIN_TAG = "task_fragment";
    private static final String TAG = RetainFragment.class.getSimpleName();
    private AsyncTask mTask;
    private ArrayList<ZipCodeData> mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setTask(AsyncTask task) {
        mTask = task;
    }

    public ArrayList<ZipCodeData> getData() {
        return mData;
    }

    public void clearData() {
        mData = null;
    }
    public boolean isTaskRunning() {
        return mTask == null ? false : mTask.getStatus() != AsyncTask.Status.FINISHED;
    }
    public void queryForeCast(ArrayList<String> zipCodes) {
        AsyncTask<ArrayList<String>, Void, ArrayList<ZipCodeData>> task =
                new AsyncTask<ArrayList<String>, Void, ArrayList<ZipCodeData>>() {
                    @Override
                    protected ArrayList<ZipCodeData> doInBackground(ArrayList<String>... zipCode) {
                        ArrayList<ZipCodeData> data = new WeatherApi(WeatherApi.VendorType.WU_API).getForecast(zipCode[0]);
                        Log.d(TAG, "data: " + data);
                        return data;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<ZipCodeData> data) {
                        super.onPostExecute(data);
                        if(mCallBack != null) {
                            mCallBack.onDataAvailable(data);
                        } else {
                            Log.d(TAG, " callback is null");
                        }
                        mData = data;
                    }
                };
        task.execute(zipCodes);
        setTask(task);
    }
    public AsyncTask getTask() {
        return mTask;
    }

    private OnDataAvailableCallBack mCallBack;
    public interface OnDataAvailableCallBack {
        void onDataAvailable(ArrayList<ZipCodeData> data);
    }

    public void setOnDataAvailableCallBack(OnDataAvailableCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RetainFragment.onDestroy()");
        if(mTask != null) {
            mTask.cancel(true);
        }
        mTask = null;
        mCallBack = null;
        mData = null;
    }
}

package com.chakra.wuweather;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.WeatherApi;
import com.chakra.wuweather.api.ZipCodeData;

import java.util.ArrayList;

/**
 * Created by f68dpim on 7/31/16.
 */
public class ZipCodeDailogFragment extends DialogFragment{
    public static final String TAG = ZipCodeDailogFragment.class.getSimpleName();
    private OnZipAddedListener mListner;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;

    public interface OnZipAddedListener {
        void onZipAdded(String zipCode, ZipCodeData zipCodeData);
    }

    public ZipCodeDailogFragment() {
    }

    public OnZipAddedListener getListner() {
        return mListner;
    }

    public void setListner(OnZipAddedListener listner) {
        this.mListner = listner;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.fragment_dialog_zipcode_add, null, false);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mErrorTextView = (TextView)view.findViewById(R.id.errorDesc);
        final EditText zipCode = (EditText)view.findViewById(R.id.zipCode);
        Button ok = (Button)view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify(zipCode.getText().toString());
            }
        });
        Button cancel = (Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZipCodeDailogFragment.this.dismiss();
            }
        });
        builder.setTitle(R.string.enter_zipcode);
        builder.setView(view);
        return builder.create();
    }

    private void verify(final String zipCode) {
        AsyncTask<String, Void, ZipCodeData> task = new AsyncTask<String, Void, ZipCodeData>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected ZipCodeData doInBackground(String... params) {
                ZipCodeData data = new WeatherApi(WeatherApi.VendorType.WU_API).getForecast(zipCode);
                Log.d(TAG, "data: " + data);
                return data;
            }

            @Override
            protected void onPostExecute(ZipCodeData zipCodeData) {
                mProgressBar.setVisibility(View.GONE);
                if(zipCodeData.getError() != null) {
                    mErrorTextView.setVisibility(View.VISIBLE);
                    mErrorTextView.setText(zipCodeData.getError().getMsg());
                } else {
                    mErrorTextView.setVisibility(View.GONE);
                    mListner.onZipAdded(zipCode, zipCodeData);
                    ZipCodeDailogFragment.this.dismiss();
                }
            }
        };
        task.execute(zipCode);

    }
}

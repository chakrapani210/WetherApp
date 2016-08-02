package com.chakra.wuweather;

import android.app.Dialog;
import android.content.Context;
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

import com.chakra.wuweather.api.Error;
import com.chakra.wuweather.api.IWeatherApi;
import com.chakra.wuweather.api.WeatherApi;
import com.chakra.wuweather.api.ZipCodeData;

/**
 * Created by f68dpim on 7/31/16.
 */
public class ZipCodeDialogFragment extends DialogFragment{
    public static final String TAG = ZipCodeDialogFragment.class.getSimpleName();
    private OnZipAddedListener mListner;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;

    public interface OnZipAddedListener {
        void onZipAdded(String zipCode, ZipCodeData zipCodeData);
    }

    public ZipCodeDialogFragment() {
    }

    public OnZipAddedListener getListner() {
        return mListner;
    }

    public void setListener(OnZipAddedListener listner) {
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
                ZipCodeDialogFragment.this.dismiss();
            }
        });
        builder.setTitle(R.string.enter_zipcode);
        builder.setView(view);
        return builder.create();
    }

    private void verify(final String zipCode) {
        mProgressBar.setVisibility(View.VISIBLE);
        new WeatherApi(WeatherApi.VendorType.WU_API,
                getActivity().getApplicationContext()).getForecast(zipCode, new IWeatherApi.OnResultCallback() {
            @Override
            public void onSuccess(ZipCodeData data) {
                mErrorTextView.setVisibility(View.GONE);
                mListner.onZipAdded(zipCode, data);
                ZipCodeDialogFragment.this.dismiss();
            }

            @Override
            public void onError(Error error) {
                mProgressBar.setVisibility(View.GONE);
                mErrorTextView.setVisibility(View.VISIBLE);
                mErrorTextView.setText(error.getMsg());
            }
        });
    }
}

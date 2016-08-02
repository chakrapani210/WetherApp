package com.chakra.wuweather;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chakra.wuweather.api.Error;
import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.IWeatherApi;
import com.chakra.wuweather.api.WeatherApi;
import com.chakra.wuweather.api.ZipCodeData;

/**
 * Created by f68dpim on 7/30/16.
 */
public class DetailsFragment extends Fragment {
    private static final String FORECAST_DATA_KEY = "forecast_data_key";
    private static final String ZIP_CODE_KEY = "zip_code_key";
    private ZipCodeData mData;
    private String mZipCode;
    private TextView mZipCodeView;
    private TextView mErrorView;
    private RecyclerView mRecyclerView;
    private ProgressDialogFragment mProgressDialogFragment;

    public static DetailsFragment getInstance(String zipCode, ZipCodeData data) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ZIP_CODE_KEY, zipCode);
        if(data != null) {
            bundle.putParcelable(FORECAST_DATA_KEY, data);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mZipCode = getArguments().getString(ZIP_CODE_KEY);
            mData = getArguments().getParcelable(FORECAST_DATA_KEY);
        }
        if(savedInstanceState != null) {
            mData = savedInstanceState.getParcelable(FORECAST_DATA_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mData != null) {
            outState.putParcelable(FORECAST_DATA_KEY, mData);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_layout, container, false);
        mZipCodeView = (TextView)view.findViewById(R.id.zipCode);
        mErrorView = (TextView)view.findViewById(R.id.errorDesc);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mZipCodeView.setText(mZipCode);
        if(mData == null) {
            queryForecast(mZipCode);
        } else {
            updateData(mData);
        }
        return view;
    }

    private void updateData(ZipCodeData data) {
        mData = data;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DetailAdapter adapter = new DetailAdapter(mData.getForecastData());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void queryForecast(String zipCode) {
        mProgressDialogFragment = new ProgressDialogFragment();
        mProgressDialogFragment.show(getActivity().getSupportFragmentManager(), ProgressDialogFragment.TAG_DAILOG);
        queryForeCast(zipCode);
    }

    private void queryForeCast(String zipCode) {
        new WeatherApi(WeatherApi.VendorType.WU_API,
                getActivity().getApplicationContext()).getForecast(zipCode, new IWeatherApi.OnResultCallback() {
            @Override
            public void onSuccess(ZipCodeData data) {
                updateData(data);
                mProgressDialogFragment.dismiss();
            }

            @Override
            public void onError(Error error) {
                mProgressDialogFragment.dismiss();
                mErrorView.setVisibility(View.VISIBLE);
                mErrorView.setText(error.getMsg());
            }
        });
    }

    public static class ProgressDialogFragment extends DialogFragment {

        public static final String TAG_DAILOG = "tag_dailog";

        public ProgressDialogFragment() {

        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle(R.string.querying_forecast_data);
            return dialog;
        }
    }
}

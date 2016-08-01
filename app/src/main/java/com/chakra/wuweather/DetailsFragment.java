package com.chakra.wuweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.ZipCodeData;

/**
 * Created by f68dpim on 7/30/16.
 */
public class DetailsFragment extends Fragment {
    private static final String FORECAST_DATA_KEY = "forecast_data_key";
    private ZipCodeData mData;


    public static DetailsFragment getInstance(ZipCodeData data) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FORECAST_DATA_KEY, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mData = getArguments().getParcelable(FORECAST_DATA_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_layout, container, false);
        TextView zipCode = (TextView)view.findViewById(R.id.zipCode);
        zipCode.setText(mData.getZipCode());
        if(mData.getError() != null) {
            TextView error = (TextView)view.findViewById(R.id.errorDesc);
            error.setVisibility(View.VISIBLE);
            error.setText(mData.getError().getMsg());
        } else {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            DetailAdapter adapter = new DetailAdapter(mData.getForecastData());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

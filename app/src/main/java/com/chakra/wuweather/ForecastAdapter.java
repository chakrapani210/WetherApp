package com.chakra.wuweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.ZipCodeData;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<ZipCodeData> mValues;
    private final WeatherFragment.OnListFragmentInteractionListener mListener;

    public ForecastAdapter(List<ZipCodeData> items, WeatherFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mZipCode.setText(holder.mItem.getZipCode());
    }

    @Override
    public int getItemCount() {
        int result = mValues == null ? 0 : mValues.size();
        //Log.d("chakra", ""+ result);

        return result;
    }

    public void setData(ArrayList<ZipCodeData> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView mZipCode;
        public ZipCodeData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mZipCode = (TextView) view.findViewById(R.id.zipCode);
        }
    }
}

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

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<ForecastData> mValues;

    public DetailAdapter(List<ForecastData> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindData(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        int result = mValues == null ? 0 : mValues.size();
        return result;
    }

    public void setData(ArrayList<ForecastData> values) {
        mValues = values;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mHighTemp;
        TextView mLowTemp;
        TextView mCondition;
        TextView mDate;
        public ForecastData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mHighTemp = (TextView)view.findViewById(R.id.highTemp);
            mLowTemp = (TextView)view.findViewById(R.id.lowTemp);
            mCondition = (TextView)view.findViewById(R.id.condition);
            mDate = (TextView)view.findViewById(R.id.date);

        }

        void bindData(ForecastData data) {
            mItem = data;
            mHighTemp.setText(data.getHigh());
            mLowTemp.setText(data.getLow());
            mCondition.setText(data.getCondition());
            mDate.setText(data.getDate());
        }
    }
}


package com.chakra.wuweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.chakra.wuweather.api.ForecastData;
import com.chakra.wuweather.api.ZipCodeData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WeatherFragment extends Fragment {

    private static final String TAG = WeatherFragment.class.getSimpleName();
    private OnListFragmentInteractionListener mListener;
    private ForecastAdapter mAdapter;
    private RetainFragment mRetainFragment;
    private ProgressDialogFragment mProgressDailogFragment;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mZipCodes = new ArrayList<String>();
    private static final String ZIP_CODE_KEY = "zip_code_key";
    private static final String ZIP_CODE_PREFERANCE_FILE = "zip_code_file";
    private ArrayList<ZipCodeData> mZipCodesData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
        mAdapter = null;
        mRetainFragment = null;
        mProgressDailogFragment = null;
        mRecyclerView = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readZipCodes();
    }

    public void readZipCodes() {
        SharedPreferences sp = getActivity().getSharedPreferences(ZIP_CODE_PREFERANCE_FILE, Context.MODE_WORLD_WRITEABLE);
        String zipCodeString= sp.getString(ZIP_CODE_KEY, null);
        String[] codes = null;
        if(zipCodeString == null) {
            codes = getResources().getStringArray(R.array.zipcodes);
        } else {
            codes = zipCodeString.split(",");
        }
        for(String code: codes) {
            mZipCodes.add(code.trim());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast_list, container, false);

        mRetainFragment = (RetainFragment)getFragmentManager().findFragmentByTag( RetainFragment.RETAIN_TAG);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZipcodeDialog();
            }
        });
        mProgressDailogFragment = new ProgressDialogFragment();
        if(mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            getFragmentManager().beginTransaction().add(mRetainFragment, RetainFragment.RETAIN_TAG).commit();
        }
        mRetainFragment.setOnDataAvailableCallBack(new RetainFragment.OnDataAvailableCallBack() {
            @Override
            public void onDataAvailable(ArrayList<ZipCodeData> data) {
                Log.d(TAG, "onDateAvailable()");
                mAdapter.setData(data);
                mProgressDailogFragment.dismiss();
            }
        });

        if (mRetainFragment.isTaskRunning()) {
                Log.d(TAG, "Task is running");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(ProgressDialogFragment.TAG_DAILOG);
                if (prev != null) {
                    ft.remove(prev);
                }
                mProgressDailogFragment.show(getActivity().getSupportFragmentManager(), ProgressDialogFragment.TAG_DAILOG);
        }

        // Set the adapter
        Log.d(TAG, "RecyclerView ***************");
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mZipCodesData = mRetainFragment.getData();
        if(mZipCodesData == null) {
            mZipCodesData = new ArrayList<ZipCodeData>();
            queryForecast();
        }
        mAdapter = new ForecastAdapter(mZipCodesData, mListener);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private void showZipcodeDialog() {
        ZipCodeDailogFragment fragment = new ZipCodeDailogFragment();
        fragment.setListner(new ZipCodeDailogFragment.OnZipAddedListener() {
            @Override
            public void onZipAdded(String zipCode, ZipCodeData zipCodeData) {
                addZipCode(zipCode, zipCodeData);
            }
        });
        fragment.show(getActivity().getSupportFragmentManager(), ZipCodeDailogFragment.TAG);
    }

    void addZipCode(String zipCode, ZipCodeData zipCodeData) {
        mZipCodes.add(zipCode);
        SharedPreferences.Editor edit = getContext().getSharedPreferences(ZIP_CODE_PREFERANCE_FILE, Context.MODE_WORLD_WRITEABLE).edit();
        edit.putString(ZIP_CODE_KEY, listToString(mZipCodes)).commit();
        if(zipCodeData != null && mZipCodesData != null) {
            mZipCodesData.add(zipCodeData);
            mAdapter.setData(mZipCodesData);
        } else {
            queryForecast();
        }
    }

    private String listToString(ArrayList<String> list) {
        Iterator<String> it = list.iterator();
        StringBuilder buffer = new StringBuilder("");
        while (it.hasNext()) {
            buffer.append(it.next());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
    public void queryForecast() {
        if (mRetainFragment.isTaskRunning()) {
            return;
        }
        mProgressDailogFragment.show(getFragmentManager(), ProgressDialogFragment.TAG_DAILOG);
        mRetainFragment.queryForeCast(mZipCodes);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public void clean() {
        if(mRetainFragment != null) {
            mRetainFragment.setOnDataAvailableCallBack(null);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(ZipCodeData item);
    }


    public static class ProgressDialogFragment extends DialogFragment {

        public static final String TAG_DAILOG = "tag_dailog";

        public ProgressDialogFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.progress_layout, container);

            return view;
        }
    }
}

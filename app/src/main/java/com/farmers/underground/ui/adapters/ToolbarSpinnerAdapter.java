package com.farmers.underground.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.farmers.underground.R;

import java.util.List;

/**
 * Created by omar
 * on 10/21/15.
 */
public class ToolbarSpinnerAdapter extends BaseAdapter {


    private Context mContext;
    private List<String> mDataList;

    public ToolbarSpinnerAdapter(Context mContext, List<String> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_statistic_spinner, viewGroup, false);
        ((TextView) convertView.findViewById(R.id.TextView)).setText(getItem(i));
        return convertView;
    }

    @Override
    public View getDropDownView(int i, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_statistic_spinner_dropdown, viewGroup, false);
        ((TextView) convertView.findViewById(R.id.TextView)).setText(getItem(i));
        return convertView;
    }


    public interface SpinnerCallback {
        void onSpinnerItemSelected(String s);
    }
}

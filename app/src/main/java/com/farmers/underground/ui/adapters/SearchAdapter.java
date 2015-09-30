package com.farmers.underground.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by omar on 9/30/15.
 */
public class SearchAdapter extends BaseAdapter {

    private List<String> hintList;


    public void setItems(List<String> hintList){
        this.hintList = hintList;
    }

    @Override
    public int getCount() {
        return hintList.size();
    }

    @Override
    public String getItem(int i) {
        return hintList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}

package com.farmers.underground.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.SearchHint;

import java.util.List;

/**
 * Created by omar on 9/30/15.
 */
public class SearchHintAdapter extends BaseAdapter {
    private List<SearchHint> hintList;

    public void setItems(List<SearchHint> hintList) {
        this.hintList = hintList;
    }

    @Override
    public int getCount() {
        return hintList.size();
    }

    @Override
    public SearchHint getItem(int i) {
        return hintList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_hint, viewGroup, false);
        ((TextView)view.findViewById(R.id.tv_HintItemSearch)).setText(getItem(i).getName());
        return view;
    }
}

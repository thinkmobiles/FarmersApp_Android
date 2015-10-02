package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.farmers.underground.R;
import com.farmers.underground.ui.models.CropsListItemDH;
import com.farmers.underground.ui.models.CropsListItemVH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 10/2/15.
 */
public class CropsListAdapter extends RecyclerView.Adapter<CropsListItemVH> {
    private int height = 0;
    private List<CropsListItemDH> dataList;

    public CropsListAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<CropsListItemDH> dataList) {
        this.dataList = dataList;
    }

    @Override
    public CropsListItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crops, parent, false);
        if (height == 0) view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (height == 0) {
                    height = view.getMeasuredWidth();
                    view.getLayoutParams().height = height;
                } else view.getLayoutParams().height = height;
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        else {
            view.getLayoutParams().height = height;
        }
        return new CropsListItemVH(view);
    }

    @Override
    public void onBindViewHolder(CropsListItemVH holder, int position) {
        holder.bindData(dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface CropsAdapterCallback {
        void onItemClicked(int pos);

        void onFavChecked(int pos, boolean isChecked);

        void onPriceRefreshClicked(int pos);
    }
}

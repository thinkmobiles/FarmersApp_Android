package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.models.CropsListItemDH;
import com.farmers.underground.ui.models.CropsListItemVH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 10/2/15.
 */
public class CropsListAdapter extends RecyclerView.Adapter<CropsListItemVH> {
    private List<CropsListItemDH> dataList;
    private int lastPosition;

    public CropsListAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<CropsListItemDH> dataList) {
        this.dataList = dataList;
    }

    @Override
    public CropsListItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crops, parent, false);
        return new CropsListItemVH(view);
    }

    @Override
    public void onBindViewHolder(CropsListItemVH holder, int position) {
        holder.bindData(dataList.get(position));
//        setAnimation(holder.getContainer(), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface CropsAdapterCallback {
        void onItemClicked(CropModel cropModel);

        void onFavChecked(CropModel cropModel, boolean isChecked);

        void onPriceRefreshClicked(CropModel cropModel);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

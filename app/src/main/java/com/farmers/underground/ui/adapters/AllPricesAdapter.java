package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.models.AllPricesDH;
import com.farmers.underground.ui.models.AllPricesVH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesAdapter extends RecyclerView.Adapter<AllPricesVH> {

    private List<AllPricesDH> dataList;
    private int lastPosition;

    public AllPricesAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<AllPricesDH> dataList) {
        this.dataList = dataList;
    }

    @Override
    public AllPricesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_prices, parent, false);
        return new AllPricesVH(view);
    }

    @Override
    public void onBindViewHolder(AllPricesVH holder, int position) {
        boolean hideDevider = position == getItemCount()-1;
        holder.bindData(dataList.get(position), hideDevider);
//        setAnimation(   holder.getContainer(), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface AllPricesCallback {
        void onAllPricesItemClicked(LastCropPricesModel cropModel); // -> ?
        void onAddPricesClicked(LastCropPricesModel cropModel);
        void onMorePricesClicked(LastCropPricesModel cropModel);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;/*
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;*/
import android.widget.CheckBox;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.models.CropsListItemDH;
import com.farmers.underground.ui.models.CropsListItemVH;
import com.farmers.underground.ui.utils.ImageCacheManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/2/15.
 */
public class CropsListAdapter extends RecyclerView.Adapter<CropsListItemVH> {

    private List<CropsListItemDH> dataList;

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    private static final ImageLoader imageLoader = ImageCacheManager.getImageLoader(FarmersApp.ImageLoaders.CACHE_MAIN);

    public CropsListAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<CropsListItemDH> dataList) {
        this.dataList  =  dataList ;
    }


    @Override
    public CropsListItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crops, parent, false);
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
        void onItemClicked(LastCropPricesModel cropModel);

        void onFavChecked(LastCropPricesModel cropModel, final CheckBox cb);

        void onPriceRefreshClicked(LastCropPricesModel cropModel);
    }
}

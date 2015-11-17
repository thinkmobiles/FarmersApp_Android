package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.CropPrices;
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

    public AllPricesAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<AllPricesDH> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataList(List<AllPricesDH> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public AllPricesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_prices, parent, false);
        return new AllPricesVH(view);
    }

    @Override
    public void onBindViewHolder(AllPricesVH holder, int position) {
        boolean hideDivider = position == getItemCount()-1;
        holder.bindData(dataList.get(position), hideDivider);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface AllPricesCallback {
        void onAddPricesClicked(String date);
        void onMorePricesClicked(CropPrices priceModel);
    }
}

package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.ui.models.DateMarketeerPricesVH;
import com.farmers.underground.ui.models.PriceMarketeerPricesDH;
import com.farmers.underground.ui.models.PriceMarketeerPricesVH;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 10/2/15.
 */
public class MarketeerPricesAdapter extends RecyclerView.Adapter<PriceMarketeerPricesVH> implements StickyRecyclerHeadersAdapter<DateMarketeerPricesVH> {

    private List<PriceMarketeerPricesDH> dataList;

    public MarketeerPricesAdapter() {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<PriceMarketeerPricesDH> dataList) {
        this.dataList = dataList;
    }

    @Override
    public PriceMarketeerPricesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketeer_price, parent, false);
        return new PriceMarketeerPricesVH(view);

    }

    @Override
    public void onBindViewHolder(PriceMarketeerPricesVH holder, int position) {
        boolean hideDivider = position == getItemCount() - 1;

        if(position < getItemCount() -1 && getHeaderId(position) != getHeaderId(position +1))
            hideDivider = true;
        holder.bindData(dataList.get(position), hideDivider);
    }

    @Override
    public long getHeaderId(int position) {
        return Math.abs(dataList.get(position).getDateHashCode());
    }

    @Override
    public DateMarketeerPricesVH onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketeer_price_date, parent, false);
        return new DateMarketeerPricesVH(view);
    }

    @Override
    public void onBindHeaderViewHolder(DateMarketeerPricesVH holder, int position) {
        holder.bindData(dataList.get(position), false);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface Callback {
        void onMorePricesClicked(MarketeerPriceModel marketeerPriceModel);

        void onNoPricesClicked(MarketeerPriceModel model);
    }

}

package com.farmers.underground.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.farmers.underground.R;
import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.ui.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 10/2/15.
 */
public class MarketeerPricesAdapter extends RecyclerView.Adapter<BaseMarketeerPricesVH> {

    private List<BaseMarketeerPricesDH> dataList;
    private int lastPosition;


    public MarketeerPricesAdapter( ) {
        dataList = new ArrayList<>();
    }

    public void setDataList(List<BaseMarketeerPricesDH> dataList) {
        this.dataList = dataList;
    }

    @Override
    public BaseMarketeerPricesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketeer_price, parent, false);
                return new PriceMarketeerPricesVH(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketeer_price_date, parent, false);
                return new DateMarketeerPricesVH(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseMarketeerPricesVH holder, int position) {
        boolean hideDevider = position == getItemCount() - 1;
        holder.bindData(dataList.get(position), hideDevider);
        setAnimation(holder.getContainer(), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if( dataList.get(position) instanceof PriceMarketeerPricesDH)
            return 0;
        else
            return 1;
    }

    public interface Callback{
        void onMorePricesClicked(MarketeerPriceModel marketeerPriceModel);

        void onNoPricesClicked(MarketeerPriceModel model);
    }

}

package com.farmers.underground.ui.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.ButterKnife;

/**
 * Created by omar on 10/2/15.
 */
abstract public class BaseMarketeerPricesVH extends RecyclerView.ViewHolder {



    public BaseMarketeerPricesVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    abstract public void bindData(BaseMarketeerPricesDH dataHolder, boolean hideDevider);

    abstract public View getContainer();

}

package com.farmers.underground.ui.models;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;

import com.farmers.underground.R;

/**
 * Created by omar
 * on 10/2/15.
 */
public class DateMarketeerPricesVH extends BaseMarketeerPricesVH {

    @Bind(R.id.tv_Date_MP)
    protected TextView tv_Date;

    @Bind(R.id.dateConainer_MP)
    protected View container;

    public DateMarketeerPricesVH(final View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(PriceMarketeerPricesDH dataHolder, boolean hideDivider) {
        tv_Date.setText(dataHolder.getModel().getDisplayDate());
    }

    @Override
    public View getContainer() {
        return container;
    }
}

package com.farmers.underground.ui.models;

import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.farmers.underground.R;

/**
 * Created by omar on 10/2/15.
 */
  public class DateMarketeerPricesVH extends BaseMarketeerPricesVH {

    @Bind(R.id.tv_Date_MP)
    protected TextView tv_Date;
    @Bind(R.id.dateConainer_MP)
    protected View conainer;

    public DateMarketeerPricesVH(final View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(BaseMarketeerPricesDH dataHolder, boolean hideDevider) {
        tv_Date.setText(((DateMarketeerPricesDH) dataHolder).getDate());
    }



    @Override
    public View getContainer() {
        return conainer;
    }
}

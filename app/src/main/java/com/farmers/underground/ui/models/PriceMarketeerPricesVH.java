package com.farmers.underground.ui.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.farmers.underground.R;

/**
 * Created by omar on 10/2/15.
 */
public class PriceMarketeerPricesVH extends BaseMarketeerPricesVH {


    @Bind(R.id.tv_Price)
    protected TextView tv_Price;

    @Bind(R.id.tv_MarketeerArea_MP)
    protected TextView tv_MarketeerArea;

    @Bind(R.id.tv_MarketeerName_MP)
    protected TextView tv_MarketeerName;

    @Bind(R.id.tv_MarketeerLogoText_MP)
    protected TextView tv_MarketeerLogoText;

    @Bind(R.id.iv_MarketeerLogoImage_MP)
    protected ImageView iv_MarketeerLogoImage;

    @Bind(R.id.rv_container_price_item_MP)
    protected View container;

    @Bind(R.id.v_Devider_MP)
    protected View devider;


    public PriceMarketeerPricesVH(final View itemView) {
        super(itemView);


    }

    @Override
    public void bindData(BaseMarketeerPricesDH dataHolder, boolean hideDevider) {
        devider.setVisibility(hideDevider ? View.GONE : View.VISIBLE);

        ((PriceMarketeerPricesDH) dataHolder).getModel();
    }



    @Override
    public View getContainer() {
        return container;
    }


}

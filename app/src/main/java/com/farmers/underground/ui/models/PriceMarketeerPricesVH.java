package com.farmers.underground.ui.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
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

    @Bind(R.id.tv_why_c_no_price_MP)
    protected TextView tv_why_c_no_price_MP;

    @Bind(R.id.v_Devider_MP)
    protected View devider;

    private PriceMarketeerPricesDH dataHolder;

    public PriceMarketeerPricesVH(final View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(BaseMarketeerPricesDH dataHolder, boolean hideDevider) {
        devider.setVisibility(hideDevider ? View.GONE : View.VISIBLE);
        this.dataHolder = ((PriceMarketeerPricesDH) dataHolder);
    }

    @Override
    public View getContainer() {
        return container;
    }

    @OnClick(R.id.tv_why_c_no_price_MP)
    protected void onMorePriceClick(){dataHolder.getCallback().onMorePricesClicked(dataHolder.getModel());}
}

package com.farmers.underground.ui.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.farmers.underground.R;

/**
 * Created by omar
 * on 10/2/15.
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

    @Bind(R.id.ll_why_c_container_MP)
    protected LinearLayout ll_why_c_container_MP;

    @Bind(R.id.ll_price_text_conainer_MP)
    protected View v_PriceContainer;

    @Bind(R.id.v_Devider_MP)
    protected View devider;

    @Bind(R.id.tv_MorePrice_MP)
    protected TextView tv_MorePrice;

    private PriceMarketeerPricesDH dataHolder;

    public PriceMarketeerPricesVH(final View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(BaseMarketeerPricesDH dataHolder, boolean hideDevider) {
        devider.setVisibility(hideDevider ? View.GONE : View.VISIBLE);
        this.dataHolder = ((PriceMarketeerPricesDH) dataHolder);
        showRandom();
    }

    @Override
    public View getContainer() {
        return container;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tv_why_c_no_price_MP)
    protected void onNoPriceClick() {
        dataHolder.getCallback().onNoPricesClicked(dataHolder.getModel());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tv_MorePrice_MP)
    protected void onMorePriceClick() {
        dataHolder.getCallback().onMorePricesClicked(dataHolder.getModel());
    }

    private void showRandom() {
        boolean isShow = Math.random() > 0.5f;
        v_PriceContainer.setVisibility(!isShow ? View.VISIBLE : View.GONE);
        ll_why_c_container_MP.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tv_MorePrice.setVisibility(Math.random() > 0.5f ? View.VISIBLE : View.GONE);
    }


}

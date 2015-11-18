package com.farmers.underground.ui.models;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.farmers.underground.R;
import com.farmers.underground.ui.utils.StringFormaterUtil;

/**
 * Created by omar
 * on 10/2/15.
 */
public class PriceMarketeerPricesVH extends BaseMarketeerPricesVH {

    @Bind(R.id.tv_Price_Mrktr)
    protected TextView tv_Price_Mrktr;

    @Bind(R.id.tv_MarketeerArea_MP)
    protected TextView tv_MarketeerArea;

    @Bind(R.id.tv_MarketeerName_MP)
    protected TextView tv_MarketeerName;

    @Bind(R.id.tv_MarketeerLogoText_MP)
    protected TextView tv_MarketeerLogoText;

    @Bind(R.id.rv_container_price_item_MP)
    protected View container;

    @Bind(R.id.ll_why_c_container_MP)
    protected LinearLayout ll_why_c_container_MP;

    @Bind(R.id.ll_price_text_conainer_MP)
    protected View v_PriceContainer;

    @Bind(R.id.v_Devider_MP)
    protected View divider;

    @Bind(R.id.tv_MorePrice_MP)
    protected TextView tv_MorePrice;

    private PriceMarketeerPricesDH dataHolder;

    public PriceMarketeerPricesVH(final View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(PriceMarketeerPricesDH dataHolder, boolean hideDivider) {
        divider.setVisibility(hideDivider ? View.GONE : View.VISIBLE);
        this.dataHolder = dataHolder;

        tv_MarketeerName.setText(dataHolder.getModel().getName());
        tv_MarketeerArea.setText(dataHolder.getModel().getLocation());
        tv_MarketeerLogoText.setText(StringFormaterUtil.getLettersForLogo(dataHolder.getModel().getName()));


        boolean shoulShowWhyTextInsteadPrice = dataHolder.getModel().getPrice() == null;

        v_PriceContainer.setVisibility(!shoulShowWhyTextInsteadPrice ? View.VISIBLE : View.GONE);
        ll_why_c_container_MP.setVisibility(shoulShowWhyTextInsteadPrice ? View.VISIBLE : View.GONE);

        if(!shoulShowWhyTextInsteadPrice){
            tv_Price_Mrktr.setText(dataHolder.getModel().getPriceDisplay());
        }

        tv_MorePrice.setVisibility((!shoulShowWhyTextInsteadPrice && !dataHolder.getModel().getMore().isEmpty()) ? View.VISIBLE : View.GONE);

//      boolean isShow = Math.random() > 0.5f;
//      v_PriceContainer.setVisibility(!isShow ? View.VISIBLE : View.GONE);
//      ll_why_c_container_MP.setVisibility(isShow ? View.VISIBLE : View.GONE);
//      tv_MorePrice.setVisibility(Math.random() > 0.5f ? View.VISIBLE : View.GONE);

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

}

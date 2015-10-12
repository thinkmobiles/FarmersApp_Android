package com.farmers.underground.ui.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;

/**
 * Created by omar on 10/2/15.
 */
public class AllPricesVH extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_DayAllPrices)
    protected TextView tv_Number;

    @Bind(R.id.tv_MonthCalendarAllPrices)
    protected TextView tv_MonthWord;

    @Bind(R.id.tv_YearAllPrices)
    protected TextView tv_YearText;

    @Bind(R.id.ll_PricesContainer_CropItem)
    protected LinearLayout ll_PriceContainer;

    @Bind(R.id.v_Devider_AllPrices)
    View devider;

    @Bind(R.id.all_prices_item_view)
    protected View container;

    public View getContainer() {
        return container;
    }

    private AllPricesDH dateHolder;

    public AllPricesVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void bindData(AllPricesDH dateHolder, boolean hideDevider) {
        this.dateHolder = dateHolder;
        for (int i = 0; i < ll_PriceContainer.getChildCount(); i++) {
            TextView tv_refresh = (TextView) ll_PriceContainer.getChildAt(i)
                    .findViewById(R.id.tv_RefresPrice_CropsItem);
            if (Math.random() > 0.5f && tv_refresh != null) {
                tv_refresh.setVisibility(View.GONE);
            } else if (tv_refresh != null) tv_refresh.setVisibility(View.VISIBLE);
        }
        if (hideDevider) devider.setVisibility(View.GONE);
        else devider.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.ll_PricesContainer_CropItem)
    protected void onImageCLicked() {
        dateHolder.getCallback().onAllPricesItemClicked(dateHolder.getModel());
    }

    @OnClick(R.id.tv_RefresPrice_CropsItem)
    protected void onRefreshClicked() {
        dateHolder.getCallback().onAllPricesMorePricesClicked(dateHolder.getModel());
        // container.getContext().startActivity(new Intent( container.getContext(), AddPriceActivity.class));
    }

}

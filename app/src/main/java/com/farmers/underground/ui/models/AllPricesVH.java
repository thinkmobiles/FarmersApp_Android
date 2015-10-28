package com.farmers.underground.ui.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.PricesByDateModel;
import com.farmers.underground.remote.models.base.PriceBase;
import com.farmers.underground.ui.utils.DateHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesVH extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_DayAllPrices)
    protected TextView tv_Number;

    @Bind(R.id.tv_MonthCalendarAllPrices)
    protected TextView tv_MonthWord;

    @Bind(R.id.tv_YearAllPrices)
    protected TextView tv_YearText;

    @Bind(R.id.layout_marketer)
    protected LinearLayout layout_marketer;

    @Bind(R.id.layout_market_one)
    protected LinearLayout layout_market_one;

    @Bind(R.id.layout_market_two)
    protected LinearLayout layout_market_two;

    @Bind(R.id.ll_PricesContainer_CropItem)
    protected LinearLayout ll_PriceContainer;

    @Bind(R.id.v_Devider_AllPrices)
    View devider;

    @Bind(R.id.all_prices_item_view)
    protected View container;

    private static final int TYPE_ADD = 1;
    private static final int TYPE_MORE = 2;

    public View getContainer() {
        return container;
    }

    private AllPricesDH dateHolder;
    private LinearLayout[] layouts;

    public AllPricesVH(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        layouts = new LinearLayout[3];
        layouts[0] = layout_marketer;
        layouts[1] = layout_market_one;
        layouts[2] = layout_market_two;
    }

    public void bindData(final AllPricesDH dateHolder, boolean hideDevider) {

        this.dateHolder = dateHolder;

        List<PriceBase> prices =  dateHolder.getModel2().prices;

        for(int i = 0; i < 3; ++i) {
            setSourse(layouts[i].findViewById(R.id.tv_Marketeer_CropItem), prices.get(i).source.name);
            setPrice(layouts[i].findViewById(R.id.tv_Price), prices.get(i).price);
            if(i == 0){
                setVisibilityAndListener(layouts[i].findViewById(R.id.tv_RefresPrice_CropsItem), i);
            } else {
                setVisibilityAndListener(layouts[i].findViewById(R.id.tv_RefresPrice_CropsItem), i);
            }
        }

        setDate(prices.get(0).data);

        if (hideDevider)
            devider.setVisibility(View.GONE);
        else
            devider.setVisibility(View.VISIBLE);
    }

    private void setSourse(View tvSourse, String nameSourse){
        ((TextView) tvSourse).setText(nameSourse);
    }

    private void setPrice(View tvPrice, Double price){
        ((TextView) tvPrice).setText(price != 0 ? String.format("%.2f", price) : "- -");
    }

    private void setVisibilityAndListener(View view, final int pos){
        final PriceBase priceBase = dateHolder.getModel2().prices.get(pos);
        if(pos != 0) {
            view.setVisibility(priceBase.more.size() > 0 ? View.VISIBLE : View.INVISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateHolder.getCallback().onMorePricesClicked(priceBase);
                }
            });
            ((TextView) view).setText(R.string.more_prices);
        } else {
            view.setVisibility(priceBase.more.size() < 10 ? View.VISIBLE : View.INVISIBLE);
            layout_marketer.findViewById(R.id.tv_RefresPrice_CropsItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dateHolder.getCallback().onAddPricesClicked();
                }
            });
        }
    }

    private void setDate(String day){
        SimpleDateFormat format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
        try {
            long time = format.parse(day).getTime();
            String[] date = DateHelper.getInstance(container.getContext()).getDate(time);
            tv_Number.setText(date[0]);
            tv_MonthWord.setText(date[1]);
            tv_YearText.setText(date[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ll_PricesContainer_CropItem)
    protected void onImageCLicked() {
        dateHolder.getCallback().onAllPricesItemClicked(dateHolder.getModel());
    }

    @OnClick(R.id.tv_RefresPrice_CropsItem)
    protected void onRefreshClicked() {
//        if( true){
//            dateHolder.getCallback().onAddPricesClicked(dateHolder.getModel());
//        } else  {
//            dateHolder.getCallback().onMorePricesClicked(dateHolder.getModel());
//        }
    }

}
